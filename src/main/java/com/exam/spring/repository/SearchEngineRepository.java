package com.exam.spring.repository;

import com.exam.spring.dto.*;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;
import com.exam.spring.exception.ServerErrorException;
import com.exam.spring.helpers.OrderByName;
import com.exam.spring.helpers.OrderByPrice;
import com.exam.spring.interfaces.IOrder;
import com.exam.spring.interfaces.ISearchEngineRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SearchEngineRepository implements ISearchEngineRepository {
    private List<ProductDTO> products;
    private List<BucketResponseDTO> buckets = new ArrayList<>();

    public SearchEngineRepository() throws ServerErrorException {
        this.products = loadDatabase();
    }

    @Override
    public List<ProductDTO> loadDatabase() throws ServerErrorException {
        File file = null;

        try{
            file = ResourceUtils.getFile("classpath:products.json");
        }catch (FileNotFoundException e){
            StatusCodeDTO statusCodeDTO = new StatusCodeDTO("Cannot establish connection with the server", HttpStatus.INTERNAL_SERVER_ERROR);
            throw new ServerErrorException(statusCodeDTO);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<ProductDTO>> typeRef = new TypeReference<>() {};
        List<ProductDTO> productsList = null;

        try{
            productsList = objectMapper.readValue(file, typeRef);
        }catch (Exception e) {
            StatusCodeDTO statusCodeDTO = new StatusCodeDTO("Cannot establish connection with the server", HttpStatus.INTERNAL_SERVER_ERROR);
            throw new ServerErrorException(statusCodeDTO);
        }

        return productsList;
    }

    @Override
    public List<ProductDTO> getProducts() {
        return this.products;
    }

    @Override
    public List<ProductDTO> filterByCategory(String category, List<ProductDTO> listToFilter){
        return listToFilter.stream().filter(product -> product.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> filterByName(String name, List<ProductDTO> listToFilter) {
        return listToFilter.stream().filter(product -> product.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> filterByBrand(String brand, List<ProductDTO> listToFilter) {
        return listToFilter.stream().filter(product -> product.getBrand().equalsIgnoreCase(brand)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> filterByPrice(Double price, List<ProductDTO> listToFilter) {
        return listToFilter.stream().filter(product -> product.getPrice().compareTo(price) == 0).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> filterByFreeShipping(Boolean freeShipping, List<ProductDTO> listToFilter) {
        return listToFilter.stream().filter(product -> product.getFreeShipping().equals(freeShipping)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> filterByPrestige(Integer prestige, List<ProductDTO> listToFilter) {
        return listToFilter.stream().filter(product -> product.getPrestige().compareTo(prestige) == 0).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> orderProducts(List<ProductDTO> productsList, Integer order) {
        IOrder orderMethod;

        if (!(order == null)) {
            switch (order){
                case 0:
                    orderMethod = new OrderByName(true);
                    break;
                case 1:
                    orderMethod = new OrderByName(false);
                    break;
                case 2:
                    orderMethod = new OrderByPrice(false);
                    break;
                default:
                    orderMethod = new OrderByPrice(true);
                    break;
            }
        }else{
            orderMethod = new OrderByPrice(true);
        }


        return orderMethod.orderList(productsList);
    }

    @Override
    public List<ProductDTO> getProductsWithFilter(String filterKey, String filterValue, List<ProductDTO> motherList, Integer order) {
        switch(filterKey)
        {
            case "name":
                motherList = this.filterByName(filterValue, motherList);
                break;
            case "category":
                motherList = this.filterByCategory(filterValue, motherList);
                break;
            case "brand":
                motherList = this.filterByBrand(filterValue, motherList);
                break;
            case "price":
                motherList = this.filterByPrice(Double.valueOf(filterValue), motherList);
                break;
            case "prestige":
                motherList = this.filterByPrestige(Integer.valueOf(filterValue), motherList);
                break;
            case "freeShipping":
                motherList = this.filterByFreeShipping(Boolean.valueOf(filterValue), motherList);
                break;
        }

        return orderProducts(motherList, order);
    }

    @Override
    public ProductDTO getProductById(Integer id) throws ProductNotFoundException {
        Optional<ProductDTO> product = this.products.stream().filter(productDTO -> productDTO.getId().equals(id)).findFirst();
        if (product.isEmpty()) {
            StatusCodeDTO statusCodeDTO = new StatusCodeDTO("Product with id " + id + " not found", HttpStatus.NOT_FOUND);
            throw new ProductNotFoundException(statusCodeDTO);
        }
        return product.get();
    }

    @Override
    public void checkStock(ProductDTO product, Integer quantity) throws InsufficientStockException, ProductNotFoundException {
        if (product.getStock() < quantity) {
            StatusCodeDTO statusCodeDTO = new StatusCodeDTO("Product with id " + product.getId() + " has insufficient stock", HttpStatus.BAD_REQUEST);
            throw new InsufficientStockException(statusCodeDTO);
        }
    }

    @Override
    public void checkStock(List<PurchaseDTO> purchase) throws InsufficientStockException, ProductNotFoundException {
        for (PurchaseDTO purchaseDTO : purchase) {
            ProductDTO product = getProductById(purchaseDTO.getProductId());
            checkStock(product, purchaseDTO.getQuantity());
        }
    }

    @Override
    public void updateStock(ProductDTO product, Integer quantity) {
        Integer index = 0;
        for (ProductDTO productDTO : products) {
            if (productDTO.getId().equals(product.getId())) break;
            index++;
        }

        products.get(index).setStock(product.getStock() - quantity);
    }

    @Override
    public Double buyProduct(ProductDTO product, Integer quantity) {
        updateStock(product, quantity);
        return (quantity * product.getPrice());
    }

    @Override
    public Double buyProducts(List<PurchaseDTO> products) throws ProductNotFoundException {
        Double total = 0d;
        for (PurchaseDTO purchaseDTO : products) {
            ProductDTO product = getProductById(purchaseDTO.getProductId());
            total += buyProduct(product, purchaseDTO.getQuantity());
        }

        return total;
    }

    @Override
    public BucketResponseDTO createBucket(Integer bucketId) {
        BucketResponseDTO bucket = new BucketResponseDTO();
        bucket.setArticles(new ArrayList<>());
        bucket.setTotal(0d);
        bucket.setId(bucketId);
        this.buckets.add(bucket);
        return bucket;
    }

    @Override
    public Optional<BucketResponseDTO> getBucket(Integer bucketId){
        return this.buckets.stream().filter(bucket -> bucket.getId().equals(bucketId)).findFirst();
    }

    @Override
    public void updateBucketValues(BucketResponseDTO bucket, ProductDTO product, Integer quantity) {
        Optional<PurchaseDTO> purchaseDTO = bucket.getArticles().stream().filter(purchase -> purchase.getProductId().equals(product.getId())).findFirst();

        if (purchaseDTO.isPresent()){
            purchaseDTO.get().setQuantity(purchaseDTO.get().getQuantity() + quantity);
        }else{
            PurchaseDTO purchase = new PurchaseDTO();
            purchase.setBrand(product.getBrand());
            purchase.setProductId(product.getId());
            purchase.setQuantity(quantity);
            purchase.setName(product.getName());
            bucket.getArticles().add(purchase);
        }

        bucket.setTotal(bucket.getTotal() + buyProduct(product, quantity));
    }

    @Override
    public BucketResponseDTO addToBucket(BucketResponseDTO bucket, Integer productId, Integer quantity) throws InsufficientStockException, ProductNotFoundException {
        ProductDTO product = getProductById(productId);
        checkStock(product, quantity);
        updateBucketValues(bucket, product, quantity);
        return bucket;
    }

}

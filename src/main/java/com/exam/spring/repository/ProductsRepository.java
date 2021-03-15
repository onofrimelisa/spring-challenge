package com.exam.spring.repository;

import com.exam.spring.dto.*;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;
import com.exam.spring.exception.ServerErrorException;
import com.exam.spring.helpers.FilterUtils;
import com.exam.spring.helpers.OrderByName;
import com.exam.spring.helpers.OrderByPrice;
import com.exam.spring.helpers.StatusCode;
import com.exam.spring.interfaces.IOrder;
import com.exam.spring.interfaces.IProductsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class ProductsRepository implements IProductsRepository {
    private List<ProductDTO> products;

    public ProductsRepository() throws ServerErrorException {
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
    public void orderProducts(List<ProductDTO> productsList, Integer order) {
        IOrder orderMethod;

        if (order != null) {
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

        orderMethod.orderList(productsList);
    }

    @Override
    public List<ProductDTO> getProductsWithFilter(Map<String, String> filters, List<ProductDTO> productList) {
        List<Predicate<ProductDTO>> allPredicates = new ArrayList<>();

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            switch(filter.getKey())
            {
                case "name":
                    allPredicates.add(FilterUtils.filterProductsByName(filter.getValue()));
                    break;
                case "category":
                    allPredicates.add(FilterUtils.filterProductsByCategory(filter.getValue()));
                    break;
                case "brand":
                    allPredicates.add(FilterUtils.filterProductsByBrand(filter.getValue()));
                    break;
                case "price":
                    allPredicates.add(FilterUtils.filterProductsByPrice(Double.valueOf(filter.getValue())));
                    break;
                case "prestige":
                    allPredicates.add(FilterUtils.filterProductsByPrestige(Integer.valueOf(filter.getValue())));
                    break;
                case "freeShipping":
                    allPredicates.add(FilterUtils.filterProductsByFreeShipping(Boolean.valueOf(filter.getValue())));
                    break;
            }
        }

        return productList
            .stream()
            .filter(allPredicates.stream().reduce(x->true, Predicate::and))
            .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Integer id) throws ProductNotFoundException {
        Optional<ProductDTO> product = this.products.stream().filter(productDTO -> productDTO.getId().equals(id)).findFirst();
        if (product.isEmpty())
            throw new ProductNotFoundException(StatusCode.getCustomStatusCode("Product with id " + id + " not found", HttpStatus.NOT_FOUND));
        return product.get();
    }

    @Override
    public void checkStock(ProductDTO product, Integer quantity) throws InsufficientStockException {
        if (product.getStock() < quantity)
            throw new InsufficientStockException(StatusCode.getCustomStatusCode("Product with id " + product.getId() + " has insufficient stock", HttpStatus.BAD_REQUEST));
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
        int index = 0;
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
}

package com.exam.spring.repository;

import com.exam.spring.dto.ErrorDTO;
import com.exam.spring.dto.ProductDTO;
import com.exam.spring.exception.ServerErrorException;
import com.exam.spring.interfaces.ISearchEngineRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SearchEngineRepository implements ISearchEngineRepository {
    private List<ProductDTO> products;

    public SearchEngineRepository() throws ServerErrorException {
        this.products = loadDatabase();
    }

    @Override
    public List<ProductDTO> loadDatabase() throws ServerErrorException {
        File file = null;

        try{
            file = ResourceUtils.getFile("classpath:products.json");
        }catch (FileNotFoundException e){
            ErrorDTO errorDTO = new ErrorDTO("Cannot establish connection with the server", HttpStatus.INTERNAL_SERVER_ERROR);
            throw new ServerErrorException(errorDTO);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<ProductDTO>> typeRef = new TypeReference<>() {};
        List<ProductDTO> productsList = null;

        try{
            productsList = objectMapper.readValue(file, typeRef);
        }catch (Exception e) {
            ErrorDTO errorDTO = new ErrorDTO("Cannot establish connection with the server", HttpStatus.INTERNAL_SERVER_ERROR);
            throw new ServerErrorException(errorDTO);
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
    public List<ProductDTO> getProductsWithFilter(String filterKey, String filterValue, List<ProductDTO> motherList) {
        switch(filterKey)
        {
            case "price":
                motherList = this.filterByPrice(Double.valueOf(filterValue), motherList);
                break;
            case "name":
                motherList = this.filterByName(filterValue, motherList);
                break;
            case "category":
                motherList = this.filterByCategory(filterValue, motherList);
                break;
            case "brand":
                motherList = this.filterByBrand(filterValue, motherList);
                break;
            case "prestige":
                motherList = this.filterByPrestige(Integer.valueOf(filterValue), motherList);
                break;
            case "freeShipping":
                motherList = this.filterByFreeShipping(Boolean.valueOf(filterValue), motherList);
                break;
        }

        return motherList;
    }
}

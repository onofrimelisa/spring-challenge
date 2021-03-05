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
}

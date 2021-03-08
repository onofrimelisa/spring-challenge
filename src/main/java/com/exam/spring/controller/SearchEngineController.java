package com.exam.spring.controller;

import com.exam.spring.dto.ErrorDTO;
import com.exam.spring.dto.ProductsListResponseDTO;
import com.exam.spring.dto.PurchaseRequestDTO;
import com.exam.spring.dto.TicketDTO;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;
import com.exam.spring.exception.SearchEngineException;
import com.exam.spring.interfaces.ISearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class SearchEngineController {
    @Autowired
    private ISearchEngineService searchEngineService;

    @GetMapping("/articles")
    public ProductsListResponseDTO getProductsFiltered(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "prestige", required = false) String prestige,
            @RequestParam(value = "freeShipping", required = false) String freeShipping,
            @RequestParam(value = "order", required = false) Integer order){
        Map<String, String> filters = new HashMap<>();

        if (category != null) filters.put("category", category);
        if (name != null) filters.put("name", name);
        if (brand != null) filters.put("brand", brand);
        if (prestige != null) filters.put("prestige", prestige);
        if (freeShipping != null) filters.put("freeShipping", freeShipping);
        if (price != null) filters.put("price", price);

        return this.searchEngineService.getProductsWithFilters(filters, order);
    }

    @PostMapping("/purchase-request")
    public TicketDTO purchaseRequest(@RequestBody PurchaseRequestDTO purchaseRequestDTO) throws InsufficientStockException, ProductNotFoundException {
        return this.searchEngineService.purchaseRequest(purchaseRequestDTO);
    }

    @ExceptionHandler(SearchEngineException.class)
    public ResponseEntity<ErrorDTO> handleSearchEngineException(SearchEngineException searchEngineException) {
        System.out.println(searchEngineException.getErrorDTO().getStatus());
        return new ResponseEntity<>(searchEngineException.getErrorDTO(), searchEngineException.getErrorDTO().getStatus());
    }
}

package com.exam.spring.controller;

import com.exam.spring.dto.*;
import com.exam.spring.exception.*;
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

    @PostMapping("/add-bucket/{bucketId}")
    public BucketResponseDTO addToBucket(@RequestBody PurchaseDTO purchaseDTO, @PathVariable Integer bucketId) throws InsufficientStockException, ProductNotFoundException {
        return this.searchEngineService.addToBucket(purchaseDTO, bucketId);
    }

    @PostMapping("/purchase-bucket/{bucketId}")
    public BucketResponseDTO addToBucket(@PathVariable Integer bucketId) throws ProductNotFoundException, BucketNotFoundException {
        return this.searchEngineService.purchaseBucket(bucketId);
    }

    @PostMapping("/customers/new")
    public CustomerDTO addCustomer(@RequestBody CustomerRequestDTO customer) throws CustomerAlreadyExistsException, InsufficientCustomersInformationException {
        return this.searchEngineService.addCustomer(customer);
    }

    @GetMapping("/customers")
    public CustomersListResponseDTO getCustomers(){
        return this.searchEngineService.getCustomers();
    }

    @ExceptionHandler(SearchEngineException.class)
    public ResponseEntity<StatusCodeDTO> handleSearchEngineException(SearchEngineException searchEngineException) {
        return new ResponseEntity<>(searchEngineException.getStatusCodeDTO(), searchEngineException.getStatusCodeDTO().getStatus());
    }
}

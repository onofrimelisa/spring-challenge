package com.exam.spring.service;

import com.exam.spring.dto.*;
import com.exam.spring.exception.BucketNotFoundException;
import com.exam.spring.exception.CustomerAlreadyExistsException;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;
import com.exam.spring.interfaces.IBucketsRepository;
import com.exam.spring.interfaces.ICustomersRepository;
import com.exam.spring.interfaces.IProductsRepository;
import com.exam.spring.interfaces.ISearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SearchEngineService implements ISearchEngineService {
    @Autowired
    private IProductsRepository productsRepository;
    @Autowired
    private IBucketsRepository bucketsRepository;
    @Autowired
    private ICustomersRepository customersRepository;

    @Override
    public ProductsListResponseDTO getProducts() {
        List<ProductDTO> products = this.productsRepository.getProducts();
        ProductsListResponseDTO response = new ProductsListResponseDTO();
        response.setArticles(products);
        response.setTotal(products.size());
        return response;
    }

    @Override
    public ProductsListResponseDTO getProductsWithFilters(Map<String, String> filters, Integer order) {
        List<ProductDTO> productsList = this.getProducts().getArticles();
        ProductsListResponseDTO response = new ProductsListResponseDTO();

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            productsList = this.productsRepository.getProductsWithFilter(filter.getKey(), filter.getValue(), productsList, order);
        }

        response.setArticles(productsList);
        response.setTotal(productsList.size());

        return response;
    }

    @Override
    public TicketDTO purchaseRequest(PurchaseRequestDTO purchaseRequestDTO) throws InsufficientStockException, ProductNotFoundException {
        List<PurchaseDTO> articles = purchaseRequestDTO.getArticles();

        this.productsRepository.checkStock(articles);

        TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
        ticketInfoDTO.setArticles(articles);
        ticketInfoDTO.setTotal(this.productsRepository.buyProducts(articles));

        TicketDTO ticket = new TicketDTO();
        ticket.setTicket(ticketInfoDTO);
        ticket.setStatusCode(new StatusCodeDTO("The purchase was done successfully", HttpStatus.OK));

        return ticket;
    }

    @Override
    public BucketResponseDTO addToBucket(PurchaseDTO purchaseDTO, Integer bucketId) throws InsufficientStockException, ProductNotFoundException {
        Optional<BucketResponseDTO> bucket = this.bucketsRepository.getBucket(bucketId);

        if(bucket.isEmpty()) bucket = Optional.of(this.bucketsRepository.createBucket(bucketId));

        BucketResponseDTO response = this.bucketsRepository.addToBucket(bucket.get(), purchaseDTO.getProductId(), purchaseDTO.getQuantity());
        response.setStatusCodeDTO(new StatusCodeDTO("Product added to the bucket successfully", HttpStatus.OK));

        return response;
    }

    @Override
    public BucketResponseDTO purchaseBucket(Integer bucketId) throws ProductNotFoundException, BucketNotFoundException {
        BucketResponseDTO response = this.bucketsRepository.purchaseBucket(bucketId);
        response.setStatusCodeDTO(new StatusCodeDTO("Products of bucket " + bucketId + " purchased successfully", HttpStatus.OK));

        return response;
    }

    @Override
    public CustomerDTO addCustomer(CustomerRequestDTO customerRequestDTO) throws CustomerAlreadyExistsException {
        String dni = customerRequestDTO.getDni();

        if (this.customersRepository.getCustomerByDNI(dni).isPresent()) {
            StatusCodeDTO statusCodeDTO = new StatusCodeDTO("The customer with the DNI " + dni + " already exists", HttpStatus.BAD_REQUEST);
            throw new CustomerAlreadyExistsException(statusCodeDTO);
        }

        return this.customersRepository.addCustomer(customerRequestDTO);
    }
}

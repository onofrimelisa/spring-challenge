package com.exam.spring.service;

import com.exam.spring.dto.*;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;
import com.exam.spring.helpers.OrderByName;
import com.exam.spring.helpers.OrderByPrice;
import com.exam.spring.interfaces.IOrder;
import com.exam.spring.interfaces.ISearchEngineRepository;
import com.exam.spring.interfaces.ISearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchEngineService implements ISearchEngineService {
    @Autowired
    private ISearchEngineRepository searchEngineRepository;

    @Override
    public ProductsListResponseDTO getProducts() {
        List<ProductDTO> products = this.searchEngineRepository.getProducts();
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
            productsList = this.searchEngineRepository.getProductsWithFilter(filter.getKey(), filter.getValue(), productsList, order);
        }

        response.setArticles(productsList);
        response.setTotal(productsList.size());

        return response;
    }

    @Override
    public TicketDTO purchaseRequest(PurchaseRequestDTO purchaseRequestDTO) throws InsufficientStockException, ProductNotFoundException {
        List<PurchaseDTO> articles = purchaseRequestDTO.getArticles();

        this.searchEngineRepository.checkStock(articles);

        TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
        ticketInfoDTO.setArticles(articles);
        ticketInfoDTO.setTotal(this.searchEngineRepository.buyProducts(articles));

        TicketDTO ticket = new TicketDTO();
        ticket.setTicket(ticketInfoDTO);
        ticket.setStatusCode(new StatusCodeDTO("The purchase was done successfully", HttpStatus.OK));

        return ticket;
    }

    @Override
    public BucketResponseDTO addToBucket(Integer productId, Integer bucketId) throws InsufficientStockException, ProductNotFoundException {
        if(!this.searchEngineRepository.existBucket(bucketId)) this.searchEngineRepository.createBucket(bucketId);
        BucketInfoDTO bucketInfo = this.searchEngineRepository.addToBucket(bucketId, productId);

        BucketResponseDTO bucketResponseDTO = new BucketResponseDTO();
        bucketResponseDTO.setBucketInfo(bucketInfo);
        bucketResponseDTO.setId(bucketId);

        return bucketResponseDTO;
    }
}

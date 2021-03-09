package com.exam.spring.service;

import com.exam.spring.dto.*;
import com.exam.spring.exception.*;
import com.exam.spring.helpers.StatusCode;
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
    public ListResponseDTO<ProductDTO> getProductsWithFilters(Map<String, String> filters, Integer order) {
        List<ProductDTO> productsList = this.productsRepository.getProducts();

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            productsList = this.productsRepository.getProductsWithFilter(filters, productsList);
        }

        this.productsRepository.orderProducts(productsList, order);

        return createList(productsList);
    }

    @Override
    public TicketResponseDTO purchaseRequest(PurchaseRequestDTO purchaseRequestDTO) throws InsufficientStockException, ProductNotFoundException {
        List<PurchaseDTO> articles = purchaseRequestDTO.getArticles();

        this.productsRepository.checkStock(articles);

        TicketInfoDTO ticketInfoDTO = new TicketInfoDTO();
        ticketInfoDTO.setArticles(articles);
        ticketInfoDTO.setTotal(this.productsRepository.buyProducts(articles));

        TicketResponseDTO ticket = new TicketResponseDTO();
        ticket.setTicket(ticketInfoDTO);
        ticket.setStatusCodeDTO(StatusCode.getSuccessfulOperationStatusCode());

        return ticket;
    }

    @Override
    public BucketResponseDTO addToBucket(PurchaseDTO purchaseDTO, Integer bucketId) throws InsufficientStockException, ProductNotFoundException {
        Optional<BucketResponseDTO> bucket = this.bucketsRepository.getBucket(bucketId);

        if(bucket.isEmpty()) bucket = Optional.of(this.bucketsRepository.createBucket(bucketId));

        BucketResponseDTO response = this.bucketsRepository.addToBucket(bucket.get(), purchaseDTO.getProductId(), purchaseDTO.getQuantity());
        response.setStatusCodeDTO(StatusCode.getSuccessfulOperationStatusCode());

        return response;
    }

    @Override
    public BucketResponseDTO purchaseBucket(Integer bucketId) throws ProductNotFoundException, BucketNotFoundException {
        BucketResponseDTO response = this.bucketsRepository.purchaseBucket(bucketId);
        response.setStatusCodeDTO(StatusCode.getSuccessfulOperationStatusCode());

        return response;
    }

    @Override
    public CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO) throws CustomerAlreadyExistsException, InsufficientCustomerInformationException {
        if (missingMandatoryField(customerRequestDTO.getDni()) ||
            missingMandatoryField(customerRequestDTO.getLastname()) ||
            missingMandatoryField(customerRequestDTO.getName()) ||
            missingMandatoryField(customerRequestDTO.getProvince()))
            throw new InsufficientCustomerInformationException(StatusCode.getCustomStatusCode("Some mandatory fields for the customer creation are missing", HttpStatus.BAD_REQUEST));

        String dni = customerRequestDTO.getDni();

        if (this.customersRepository.getCustomerByDNI(dni).isPresent())
            throw new CustomerAlreadyExistsException(StatusCode.getCustomStatusCode("The customer with the DNI " + dni + " already exists", HttpStatus.BAD_REQUEST));

        CustomerDTO newCustomer = this.customersRepository.addCustomer(customerRequestDTO);

        CustomerResponseDTO customerResponse = new CustomerResponseDTO();
        customerResponse.setStatusCodeDTO(StatusCode.getSuccessfulOperationStatusCode());
        customerResponse.setCustomer(newCustomer);

        return customerResponse;
    }

    @Override
    public ListResponseDTO<CustomerDTO> getCustomers(Map<String, String> filters) {
        List<CustomerDTO> customersList = this.customersRepository.getCustomers();

        customersList = this.customersRepository.getCustomersWithFilter(filters, customersList);

        return createList(customersList);
    }

    /* #######################################################################################################

                                            SERVICE HELPERS

     ######################################################################################################### */

    private <T> ListResponseDTO<T> createList(List<T> list) {
        ListResponseDTO<T> newList = new ListResponseDTO();
        newList.setList(list);
        newList.setTotal(list.size());
        newList.setStatusCodeDTO(StatusCode.getSuccessfulOperationStatusCode());
        return newList;
    }

    private Boolean missingMandatoryField(String field){
        return field == null || field.length() == 0;
    }
}

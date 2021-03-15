package com.exam.spring.service;

import com.exam.spring.dto.*;
import com.exam.spring.exception.*;
import com.exam.spring.helpers.StatusCode;
import com.exam.spring.interfaces.IBucketsRepository;
import com.exam.spring.interfaces.ICustomersRepository;
import com.exam.spring.interfaces.IProductsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchEngineServiceTest {
    private SearchEngineService searchEngineService;
    @Mock
    private IProductsRepository productsRepository;
    @Mock
    private IBucketsRepository bucketsRepository;
    @Mock
    private ICustomersRepository customersRepository;

    @BeforeEach
    public void setContext(){
        // initialize the mocks of this class
        initMocks(this);
        searchEngineService = new SearchEngineService(productsRepository, bucketsRepository, customersRepository);
    }

    @Test
    void getProductsWithFilters(){
        // Arrange
        List<ProductDTO> products = getListOfProducts();
        List<ProductDTO> expectedResult = getListOfProductsWithASpecificCategory();
        ListResponseDTO response = new ListResponseDTO();
        response.setTotal(products.size());
        response.setList(products);
        response.setStatusCodeDTO(StatusCode.getSuccessfulOperationStatusCode());
        HashMap<String, String> filters = new HashMap<>();
        filters.put("category", "Herramientas");
        Mockito.when(productsRepository.getProducts()).thenReturn(products);
        Mockito.when(productsRepository.getProductsWithFilter(Mockito.any(), eq(products))).thenReturn(expectedResult);

        // Act
        ListResponseDTO result = this.searchEngineService.getProductsWithFilters(filters, 0);

        // Assert
        Assertions.assertIterableEquals(expectedResult, result.getList());
        Assertions.assertEquals(2, expectedResult.size());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    @Test
    void purchaseRequestSuccess() throws InsufficientStockException, ProductNotFoundException {
        // Arrange
        PurchaseRequestDTO purchaseRequest = getPurchaseRequestDTO();
        Mockito.doNothing().when(productsRepository).checkStock(Mockito.any());

        // Act & Assert
        TicketResponseDTO result = Assertions.assertDoesNotThrow(
            ()-> this.searchEngineService.purchaseRequest(purchaseRequest)
        );
        Assertions.assertEquals(1, result.getTicket().getArticles().size());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    @Test
    void purchaseRequestThrowsInsufficientStockException() throws InsufficientStockException, ProductNotFoundException {
        // Arrange
        PurchaseRequestDTO purchaseRequest = getPurchaseRequestDTO();
        InsufficientStockException expectedException = new InsufficientStockException(StatusCode.getCustomStatusCode("Product has insufficient stock", HttpStatus.BAD_REQUEST));
        Mockito.doThrow(expectedException).when(productsRepository).checkStock(Mockito.any());

        // Act & Assert
        InsufficientStockException thrownException = Assertions.assertThrows(
            InsufficientStockException.class,
            () -> this.searchEngineService.purchaseRequest(purchaseRequest)
        );
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void purchaseRequestThrowsProductNotFoundException() throws InsufficientStockException, ProductNotFoundException {
        // Arrange
        PurchaseRequestDTO purchaseRequest = getPurchaseRequestDTO();
        ProductNotFoundException expectedException = new ProductNotFoundException(StatusCode.getCustomStatusCode("Product not found", HttpStatus.NOT_FOUND));
        Mockito.doThrow(expectedException).when(productsRepository).checkStock(Mockito.any());

        // Act & Assert
        ProductNotFoundException thrownException = Assertions.assertThrows(
            ProductNotFoundException.class,
            () -> this.searchEngineService.purchaseRequest(purchaseRequest)
        );
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void addToBucketSuccess() throws InsufficientStockException, ProductNotFoundException{
        BucketResponseDTO bucket = new BucketResponseDTO();
        bucket.setArticles(getListPurchaseDTO());
        Mockito.when(bucketsRepository.getBucket(Mockito.anyInt())).thenReturn(Optional.of(bucket));
        Mockito.when(bucketsRepository.addToBucket(Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(bucket);

        // Act & Assert
        BucketResponseDTO result = Assertions.assertDoesNotThrow(
            ()-> this.searchEngineService.addToBucket(getPurchaseDTO(), 0)
        );
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    @Test
    void addToBucketThrowsProductNotFoundException() throws InsufficientStockException, ProductNotFoundException{
        BucketResponseDTO bucket = new BucketResponseDTO();
        bucket.setArticles(getListPurchaseDTO());
        ProductNotFoundException expectedException = new ProductNotFoundException(StatusCode.getCustomStatusCode("Product not found", HttpStatus.NOT_FOUND));
        Mockito.when(bucketsRepository.getBucket(Mockito.anyInt())).thenReturn(Optional.of(bucket));
        Mockito.doThrow(expectedException).when(bucketsRepository).addToBucket(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());

        // Act & Assert
        ProductNotFoundException thrownException = Assertions.assertThrows(
            ProductNotFoundException.class,
            () -> this.searchEngineService.addToBucket(getPurchaseDTO(), 0)
        );
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void addToBucketThrowsInsufficientStockException() throws InsufficientStockException, ProductNotFoundException{
        // Arrange
        BucketResponseDTO bucket = new BucketResponseDTO();
        bucket.setArticles(getListPurchaseDTO());
        Mockito.when(bucketsRepository.getBucket(Mockito.anyInt())).thenReturn(Optional.of(bucket));
        InsufficientStockException expectedException = new InsufficientStockException(StatusCode.getCustomStatusCode("Product has insufficient stock", HttpStatus.BAD_REQUEST));
        Mockito.doThrow(expectedException).when(bucketsRepository).addToBucket(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());

        // Act & Assert
        InsufficientStockException thrownException = Assertions.assertThrows(
            InsufficientStockException.class,
            () -> this.searchEngineService.addToBucket(getPurchaseDTO(), 0)
        );
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void purchaseBucketSuccess() throws ProductNotFoundException, BucketNotFoundException {
        // Arrange
        BucketResponseDTO bucket = new BucketResponseDTO();
        bucket.setArticles(getListPurchaseDTO());
        Mockito.when(bucketsRepository.getBucket(Mockito.anyInt())).thenReturn(Optional.of(bucket));
        Mockito.when(bucketsRepository.purchaseBucket(Mockito.anyInt())).thenReturn(bucket);

        // Act & Assert
        BucketResponseDTO result = Assertions.assertDoesNotThrow(
            ()-> this.searchEngineService.purchaseBucket(0)
        );
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    @Test
    void purchaseBucketThrowsBucketNotFoundException() throws ProductNotFoundException, BucketNotFoundException {
        // Arrange
        BucketResponseDTO bucket = new BucketResponseDTO();
        bucket.setArticles(getListPurchaseDTO());
        BucketNotFoundException expectedException = new BucketNotFoundException(StatusCode.getCustomStatusCode("Bucket not found", HttpStatus.NOT_FOUND));
        Mockito.doThrow(expectedException).when(bucketsRepository).purchaseBucket(Mockito.anyInt());

        // Act & Assert
        BucketNotFoundException thrownException = Assertions.assertThrows(
                BucketNotFoundException.class,
                () -> this.searchEngineService.purchaseBucket(0)
        );
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void addCustomerSuccess(){
        // Arrange
        CustomerRequestDTO customerRequestDTO = getCustomerRequestDTO();
        CustomerDTO customer = getCustomerDTO(customerRequestDTO);
        Mockito.when(customersRepository.getCustomerByDNI(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(customersRepository.addCustomer(Mockito.any())).thenReturn(customer);

        // Act & Assert
        CustomerResponseDTO result = Assertions.assertDoesNotThrow(
            ()-> this.searchEngineService.addCustomer(customerRequestDTO)
        );
        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    @Test
    void addCustomerThrowsCustomerAlreadyExistsException() throws CustomerAlreadyExistsException{
        // Arrange
        CustomerRequestDTO customerRequestDTO = getCustomerRequestDTO();
        CustomerDTO customer = getCustomerDTO(customerRequestDTO);
        Mockito.when(customersRepository.getCustomerByDNI(Mockito.anyString())).thenReturn(Optional.of(customer));

        // Act & Assert
        CustomerAlreadyExistsException thrownException = Assertions.assertThrows(
            CustomerAlreadyExistsException.class,
            () -> this.searchEngineService.addCustomer(customerRequestDTO)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());

    }

    @Test
    void getCustomers(){
        // Arrange
        List<CustomerDTO> customersList = getListOfCustomers();
        Mockito.when(customersRepository.getCustomers()).thenReturn(customersList);
        Mockito.when(customersRepository.getCustomersWithFilter(Mockito.anyMap(), Mockito.anyList())).thenReturn(customersList);

        // Act
        ListResponseDTO<CustomerDTO> result = this.searchEngineService.getCustomers(new HashMap<>());

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(customersList, result.getList());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());

    }

    /* #######################################################################################################

                                           HELPERS

    ######################################################################################################### */

    private List<ProductDTO> getListOfProducts(){
        List<ProductDTO> products = new ArrayList<>();

        ProductDTO product1 = new ProductDTO();
        product1.setStock(5);
        product1.setBrand("Nike");
        product1.setCategory("Indumentaria");
        product1.setPrestige(5);
        product1.setPrice(500d);
        product1.setFreeShipping(true);
        product1.setName("Remera");
        product1.setId(0);

        ProductDTO product2 = new ProductDTO();
        product2.setStock(5);
        product2.setBrand("Puma");
        product2.setCategory("Indumentaria");
        product2.setPrestige(5);
        product2.setPrice(500d);
        product2.setFreeShipping(true);
        product2.setName("Pantalón");
        product2.setId(1);

        products.add(product1);
        products.add(product2);
        products.addAll(getListOfProductsWithASpecificCategory());

        return products;
    }

    private List<ProductDTO> getListOfProductsWithASpecificCategory(){
        List<ProductDTO> products = new ArrayList<>();

        ProductDTO product3 = new ProductDTO();
        product3.setStock(5);
        product3.setBrand("Hermanos");
        product3.setCategory("Herramientas");
        product3.setPrestige(5);
        product3.setPrice(500d);
        product3.setFreeShipping(true);
        product3.setName("Destornillador");
        product3.setId(2);

        ProductDTO product4 = new ProductDTO();
        product4.setStock(5);
        product4.setBrand("Cetol");
        product4.setCategory("Herramientas");
        product4.setPrestige(5);
        product4.setPrice(500d);
        product4.setFreeShipping(false);
        product4.setName("Cetol");
        product4.setId(3);

        products.add(product3);
        products.add(product4);

        return products;
    }

    private PurchaseRequestDTO getPurchaseRequestDTO(){
        PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.setArticles(getListPurchaseDTO());
        return purchaseRequest;
    }

    private List<PurchaseDTO> getListPurchaseDTO(){
        List<PurchaseDTO> purchaseList = new ArrayList<>();
        purchaseList.add(getPurchaseDTO());
        return purchaseList;
    }

    private PurchaseDTO getPurchaseDTO(){
        PurchaseDTO purchase = new PurchaseDTO();
        purchase.setQuantity(0);
        purchase.setProductId(0);
        purchase.setBrand("Foo");
        purchase.setName("Bar");
        return purchase;
    }

    private CustomerRequestDTO getCustomerRequestDTO(){
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setDni("Foo");
        customerRequestDTO.setName("Bar");
        customerRequestDTO.setLastname("Baz");
        customerRequestDTO.setProvince("qux");
        return customerRequestDTO;
    }

    private CustomerDTO getCustomerDTO(CustomerRequestDTO customerRequestDTO){
        CustomerDTO customer = new CustomerDTO();
        customer.setCustomerId(0);
        customer.setName(customerRequestDTO.getName());
        customer.setProvince(customerRequestDTO.getProvince());
        customer.setLastname(customerRequestDTO.getLastname());
        customer.setDni(customer.getDni());
        return customer;
    }

    private List<CustomerDTO> getListOfCustomers(){
        CustomerDTO customer = new CustomerDTO();
        customer.setDni("Foo");
        customer.setName("Bar");
        customer.setLastname("Baz");
        customer.setProvince("qux");

        List<CustomerDTO> customerList = new ArrayList<>();
        customerList.add(customer);

        return customerList;
    }
}

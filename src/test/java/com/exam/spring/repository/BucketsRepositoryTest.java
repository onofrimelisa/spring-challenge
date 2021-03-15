package com.exam.spring.repository;

import com.exam.spring.dto.BucketResponseDTO;
import com.exam.spring.dto.CustomerDTO;
import com.exam.spring.dto.CustomerRequestDTO;
import com.exam.spring.dto.ProductDTO;
import com.exam.spring.exception.BucketNotFoundException;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;
import com.exam.spring.exception.SearchEngineException;
import com.exam.spring.helpers.StatusCode;
import com.exam.spring.interfaces.IBucketsRepository;
import com.exam.spring.interfaces.IProductsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.mockito.MockitoAnnotations.initMocks;

public class BucketsRepositoryTest {
    private IBucketsRepository bucketsRepository;
    @Mock
    private IProductsRepository productsRepository;

    @BeforeEach
    public void setContext() {
        initMocks(this);
        this.bucketsRepository = new BucketsRepository(productsRepository);
    }

    @Test
    void createBucket(){
        // Act
        BucketResponseDTO result = this.bucketsRepository.createBucket(0);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0d, result.getTotal());
        Assertions.assertEquals(0, result.getArticles().size());
    }

    @Test
    void getExistentBucket(){
        // Arrange
        BucketResponseDTO bucket = this.bucketsRepository.createBucket(0);

        // Act
        Optional<BucketResponseDTO> result = this.bucketsRepository.getBucket(0);

        // Assert
        Assertions.assertTrue(result.isPresent());
        BucketResponseDTO bucketResult = result.get();
        Assertions.assertEquals(bucket.getArticles(), bucketResult.getArticles());
        Assertions.assertEquals(bucket.getTotal(), bucketResult.getTotal());
        Assertions.assertEquals(bucket.getId(), bucketResult.getId());
    }

    @Test
    void getNonExistentBucket(){
        // Arrange
        this.bucketsRepository.createBucket(0);

        // Act
        Optional<BucketResponseDTO> result = this.bucketsRepository.getBucket(1);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void addToBucketSuccess() throws ProductNotFoundException, InsufficientStockException {
        // Arrange
        ProductDTO product = getProductDTO();
        BucketResponseDTO bucket = this.bucketsRepository.createBucket(0);
        Mockito.when(this.productsRepository.getProductById(Mockito.anyInt())).thenReturn(product);

        // Act
        BucketResponseDTO result = this.bucketsRepository.addToBucket(bucket, 0, 1);

        // Assert
        Assertions.assertEquals(bucket, result);
        Assertions.assertEquals(1, result.getArticles().size());
        Assertions.assertEquals(100d, result.getTotal());
    }

    @Test
    void addToBucketThrowsProductNotFoundException() throws ProductNotFoundException {
        // Arrange
        BucketResponseDTO bucket = this.bucketsRepository.createBucket(0);
        ProductNotFoundException expectedException = new ProductNotFoundException(StatusCode.getCustomStatusCode("Product not found", HttpStatus.NOT_FOUND));
        Mockito.doThrow(expectedException).when(productsRepository).getProductById(Mockito.anyInt());

        // Act
        ProductNotFoundException thrownException = Assertions.assertThrows(
            ProductNotFoundException.class,
            () -> this.bucketsRepository.addToBucket(bucket, 0, 0)
        );

        // Assert
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void addToBucketThrowsInsufficientStockException() throws InsufficientStockException, ProductNotFoundException {
        // Arrange
        BucketResponseDTO bucket = this.bucketsRepository.createBucket(0);
        InsufficientStockException expectedException = new InsufficientStockException(StatusCode.getCustomStatusCode("Product with insufficient stock", HttpStatus.BAD_REQUEST));
        Mockito.doThrow(expectedException).when(productsRepository).checkStock(Mockito.any(), Mockito.anyInt());

        // Act
        InsufficientStockException thrownException = Assertions.assertThrows(
            InsufficientStockException.class,
            () -> this.bucketsRepository.addToBucket(bucket, 0, 0)
        );

        // Assert
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void purchaseBucketSuccess(){
        // Arrange
        BucketResponseDTO bucket =  this.bucketsRepository.createBucket(0);

        // Act & Assert
        BucketResponseDTO result = Assertions.assertDoesNotThrow(
            () -> this.bucketsRepository.purchaseBucket(0)
        );
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bucket, result);
    }

    @Test
    void purchaseBucketThrowsBucketNotFoundException(){
        // Arrange
        BucketResponseDTO bucket =  this.bucketsRepository.createBucket(0);

        // Act & Assert
        BucketNotFoundException thrownException = Assertions.assertThrows(
            BucketNotFoundException.class,
            () -> this.bucketsRepository.purchaseBucket(1)
        );
        Assertions.assertNotNull(thrownException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCodeDTO().getStatus());
    }

    private ProductDTO getProductDTO(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(0);
        productDTO.setPrice(100d);
        productDTO.setName("Foo");
        productDTO.setCategory("Bar");
        productDTO.setBrand("Baz");
        productDTO.setPrestige(0);
        productDTO.setStock(1);
        productDTO.setFreeShipping(true);

        return productDTO;
    }
}

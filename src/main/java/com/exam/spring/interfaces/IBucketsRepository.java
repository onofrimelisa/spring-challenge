package com.exam.spring.interfaces;

import com.exam.spring.dto.BucketResponseDTO;
import com.exam.spring.dto.ProductDTO;
import com.exam.spring.exception.BucketNotFoundException;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;

import java.util.Optional;

public interface IBucketsRepository {
    Optional<BucketResponseDTO> getBucket(Integer bucketId);
    BucketResponseDTO createBucket(Integer bucketId);
    BucketResponseDTO addToBucket(BucketResponseDTO bucket, Integer productId, Integer quantity) throws InsufficientStockException, ProductNotFoundException;
    void updateBucketValues(BucketResponseDTO bucket, ProductDTO product, Integer quantity) throws InsufficientStockException, ProductNotFoundException;
    BucketResponseDTO purchaseBucket(Integer bucketId) throws BucketNotFoundException, ProductNotFoundException;
}

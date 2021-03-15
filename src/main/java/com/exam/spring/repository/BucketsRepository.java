package com.exam.spring.repository;

import com.exam.spring.dto.BucketResponseDTO;
import com.exam.spring.dto.ProductDTO;
import com.exam.spring.dto.PurchaseDTO;
import com.exam.spring.dto.StatusCodeDTO;
import com.exam.spring.exception.BucketNotFoundException;
import com.exam.spring.exception.InsufficientStockException;
import com.exam.spring.exception.ProductNotFoundException;
import com.exam.spring.helpers.StatusCode;
import com.exam.spring.interfaces.IBucketsRepository;
import com.exam.spring.interfaces.IProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BucketsRepository implements IBucketsRepository {
    private IProductsRepository productsRepository;
    private List<BucketResponseDTO> buckets;


    @Autowired public BucketsRepository(IProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
        this.buckets = new ArrayList<>();
    }


    @Override
    public Optional<BucketResponseDTO> getBucket(Integer bucketId) {
        return this.buckets.stream().filter(bucket -> bucket.getId().equals(bucketId)).findFirst();
    }

    @Override
    public BucketResponseDTO createBucket(Integer bucketId) {
        BucketResponseDTO bucket = new BucketResponseDTO();
        bucket.setArticles(new ArrayList<>());
        bucket.setTotal(0d);
        bucket.setId(bucketId);
        this.buckets.add(bucket);
        return bucket;
    }

    @Override
    public BucketResponseDTO addToBucket(BucketResponseDTO bucket, Integer productId, Integer quantity) throws InsufficientStockException, ProductNotFoundException {
        ProductDTO product = this.productsRepository.getProductById(productId);
        updateBucketValues(bucket, product, quantity);
        return bucket;
    }

    @Override
    public void updateBucketValues(BucketResponseDTO bucket, ProductDTO product, Integer quantity) throws InsufficientStockException, ProductNotFoundException {
        Optional<PurchaseDTO> purchaseDTO = bucket.getArticles().stream().filter(purchase -> purchase.getProductId().equals(product.getId())).findFirst();

        if (purchaseDTO.isPresent()){
            Integer updatedQuantity = purchaseDTO.get().getQuantity() + quantity;
            this.productsRepository.checkStock(product, updatedQuantity);
            purchaseDTO.get().setQuantity(updatedQuantity);
        }else{
            this.productsRepository.checkStock(product, quantity);
            PurchaseDTO purchase = new PurchaseDTO();
            purchase.setBrand(product.getBrand());
            purchase.setProductId(product.getId());
            purchase.setQuantity(quantity);
            purchase.setName(product.getName());
            bucket.getArticles().add(purchase);
        }

        bucket.setTotal(bucket.getTotal() + quantity * product.getPrice());
    }

    @Override
    public BucketResponseDTO purchaseBucket(Integer bucketId) throws BucketNotFoundException, ProductNotFoundException {
        Optional<BucketResponseDTO> bucket = getBucket(bucketId);
        if (bucket.isEmpty())
            throw new BucketNotFoundException(StatusCode.getCustomStatusCode("Bucket with id " + bucketId + " was not found", HttpStatus.NOT_FOUND));
        this.productsRepository.buyProducts(bucket.get().getArticles());
        this.buckets.remove(bucket.get());
        return bucket.get();
    }
}

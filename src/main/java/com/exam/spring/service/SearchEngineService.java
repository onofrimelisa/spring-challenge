package com.exam.spring.service;

import com.exam.spring.dto.ProductDTO;
import com.exam.spring.dto.ProductsListResponseDTO;
import com.exam.spring.helpers.OrderByName;
import com.exam.spring.helpers.OrderByPrice;
import com.exam.spring.interfaces.IOrder;
import com.exam.spring.interfaces.ISearchEngineRepository;
import com.exam.spring.interfaces.ISearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ProductDTO> orderProducts(List<ProductDTO> productsList, Integer order) {
        IOrder orderMethod;

       switch (order){
            case 0:
                orderMethod = new OrderByName(true);
                break;
            case 1:
                orderMethod = new OrderByName(false);
                break;
            case 2:
                orderMethod = new OrderByPrice(false);
                break;
            default:
                orderMethod = new OrderByPrice(true);
                break;
        }

        return orderMethod.orderList(productsList);
    }

    @Override
    public ProductsListResponseDTO getProductsWithFilters(Map<String, String> filters, Integer order) {
        List<ProductDTO> productsList = this.getProducts().getArticles();
        ProductsListResponseDTO response = new ProductsListResponseDTO();

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            productsList = this.searchEngineRepository.getProductsWithFilter(filter.getKey(), filter.getValue(), productsList);
        }

        if (order != null){
            productsList = this.orderProducts(productsList, order);
        }

        response.setArticles(productsList);
        response.setTotal(productsList.size());

        return response;
    }
}

package com.exam.spring.helpers;

import com.exam.spring.dto.CustomerDTO;
import com.exam.spring.dto.ProductDTO;

import java.util.function.Predicate;

public class FilterUtils {

    // PRODUCT FILTERS

    public static Predicate<ProductDTO> filterProductsByCategory(String category){
        return (product) -> product.getCategory().equalsIgnoreCase(category);
    }

    public static Predicate<ProductDTO> filterProductsByName(String name){
        return (product) -> product.getName().equalsIgnoreCase(name);
    }

    public static Predicate<ProductDTO> filterProductsByBrand(String brand){
        return (product) -> product.getBrand().equalsIgnoreCase(brand);
    }

    public static Predicate<ProductDTO> filterProductsByPrice(Double price){
        return (product) -> product.getPrice().compareTo(price) == 0;
    }

    public static Predicate<ProductDTO> filterProductsByFreeShipping(Boolean freeShipping){
        return (product) -> product.getFreeShipping().equals(freeShipping);
    }

    public static Predicate<ProductDTO> filterProductsByPrestige(Integer prestige){
        return (product) -> product.getPrestige().compareTo(prestige) == 0;
    }

    // CUSTOMER FILTERS

    public static Predicate<CustomerDTO> filterCustomersByProvince(String province){
        return (customer) -> customer.getProvince().equalsIgnoreCase(province);
    }

    public static Predicate<CustomerDTO> filterCustomersByName(String name){
        return (customer) -> customer.getName().equalsIgnoreCase(name);
    }

}

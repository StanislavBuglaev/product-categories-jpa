package com.product.categories.jpa.service.rest.config;

import com.product.categories.jpa.service.CategoryService;
import com.product.categories.jpa.service.ProductService;
import com.product.categories.jpa.service.rest.CategoryServiceRest;
import com.product.categories.jpa.service.rest.ProductServiceRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {

    public static final String CATEGORIES_URL = "http://localhost:8088/categories";

    public static final String PRODUCTS_URL = "http://localhost:8088/products";

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    CategoryService categoryService() {
        return new CategoryServiceRest(CATEGORIES_URL, restTemplate());
    }

    @Bean
    ProductService productService() {
        return new ProductServiceRest(PRODUCTS_URL, restTemplate());
    }

}

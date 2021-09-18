package com.product.categories.jpa.web_app.config;

import com.product.categories.jpa.service.CategoryService;
import com.product.categories.jpa.service.ProductService;
import com.product.categories.jpa.service.rest.CategoryServiceRest;
import com.product.categories.jpa.service.rest.ProductServiceRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
public class ApplicationConfig {

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private Integer port;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    CategoryService categoryService() {
        String url = String.format("%s://%s:%d/categories", protocol, host, port);
        return new CategoryServiceRest(url, restTemplate());
    };

    @Bean
    ProductService productService() {
        String url = String.format("%s://%s:%d/products", protocol, host, port);
        return new ProductServiceRest(url, restTemplate());
    };
}

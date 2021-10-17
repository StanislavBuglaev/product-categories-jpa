package com.product.categories.jpa.service.rest;

import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.entity.Product;
import com.product.categories.jpa.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceRest implements ProductService {
    Logger LOGGER = LoggerFactory.getLogger(ProductServiceRest.class);

    private String url;

    private RestTemplate restTemplate;

    public ProductServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Product> findAll() {
        LOGGER.debug("findAll()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Product>) responseEntity.getBody();
    }

    @Override
    public Optional<Product> findById(Integer id) {
        LOGGER.debug("findById({})", id);
        ResponseEntity<Product> responseEntity =
                restTemplate.getForEntity(url + "/" + id, Product.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Product createOrUpdate(Product product) {
        LOGGER.debug("create({})", product);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, product, Product.class);
        return (Product) responseEntity.getBody();
    }

    @Override
    public void delete(Integer id) {
        LOGGER.debug("delete({})", id);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Product> entity = new HttpEntity<>(headers);
        ResponseEntity<Integer> result =
                restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, entity, Integer.class);
        result.getBody();
    }
}

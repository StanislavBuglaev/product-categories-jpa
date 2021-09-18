package com.product.categories.jpa.service.rest;

import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.service.CategoryService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

@Service
public class CategoryServiceRest implements CategoryService {

    Logger LOGGER = LoggerFactory.getLogger(CategoryServiceRest.class);

    private String url;

    private RestTemplate restTemplate;

    public CategoryServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Category> findAll() {
        LOGGER.debug("findAll()");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Category>) responseEntity.getBody();
    }

    @Override
    public Optional<Category> findById(Integer id) {
        LOGGER.debug("findById({})", id);
        ResponseEntity<Category> responseEntity =
                restTemplate.getForEntity(url + "/" + id, Category.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Category create(Category category) {
        LOGGER.debug("create({})", category);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, category, Category.class);
        return (Category) responseEntity.getBody();
    }

    @Override
    public Category update(Integer id) {
        LOGGER.debug("update({})", id);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Category> entity = new HttpEntity<>(findById(id).get(), headers);
        ResponseEntity<Category> result = restTemplate.exchange(url, HttpMethod.PUT, entity, Category.class);
        return result.getBody();
    }

    @Override
    public void delete(Integer id) {
        LOGGER.debug("delete({})", id);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Category> entity = new HttpEntity<>(headers);
        ResponseEntity<Integer> result =
                restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, entity, Integer.class);
        result.getBody();
    }
}

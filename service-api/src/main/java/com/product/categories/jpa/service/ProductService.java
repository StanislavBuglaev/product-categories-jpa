package com.product.categories.jpa.service;

import com.product.categories.jpa.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();

    Optional<Product> findById(Integer id);

    Product createOrUpdate(Product product);

    void delete(Integer id);
}

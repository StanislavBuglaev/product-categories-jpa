package com.product.categories.jpa.service;


import com.product.categories.jpa.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();

    Optional<Category> findById(Integer id);

    Category createOrUpdate(Category Category);

    void delete(Integer id);
}

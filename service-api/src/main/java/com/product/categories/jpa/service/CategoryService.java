package com.product.categories.jpa.service;


import com.product.categories.jpa.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();

    Optional<Category> findById(Integer id);

    Category create(Category Category);

    Category update(Integer id);

    void delete(Integer id);
}

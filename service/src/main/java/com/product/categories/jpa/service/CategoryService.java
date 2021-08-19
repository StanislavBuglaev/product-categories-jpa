package com.product.categories.jpa.service;

import com.product.categories.jpa.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(Integer categoryId);

    Category create(Category category);

    Category update(Integer categoryId);

    void delete(Integer categoryId);
}

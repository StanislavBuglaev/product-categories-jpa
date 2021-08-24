package com.product.categories.jpa.service;

import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Integer categoryId) {
        return categoryRepository.findById(categoryId);
    }


    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Integer categoryId) {
        return categoryRepository.findById(categoryId).get();
    }

    public void delete(Integer categoryId) {
         categoryRepository.deleteById(categoryId);
    }

}

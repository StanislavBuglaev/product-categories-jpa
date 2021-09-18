package com.product.categories.jpa.service;

import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        LOGGER.trace("findAll()");
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Integer categoryId) {
        LOGGER.debug("findById(id:{})", categoryId);
        return categoryRepository.findById(categoryId);
    }


    public Category create(Category category) {
        LOGGER.debug("create(product:{})", category);
        return categoryRepository.save(category);
    }

    public Category update(Integer categoryId) {
        LOGGER.debug("update(product:{})", categoryId);
        return categoryRepository.findById(categoryId).get();
    }

    public void delete(Integer categoryId) {
        LOGGER.debug("delete(productId:{})", categoryId);
        categoryRepository.deleteById(categoryId);
    }

}

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
public class CategoryServiceImpl implements CategoryService{

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        LOGGER.trace("findAll()");
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Integer categoryId) {
        LOGGER.debug("findById(id:{})", categoryId);
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Category createOrUpdate(Category category) {
        LOGGER.debug("create(category:{})", category);
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Integer categoryId) {
        LOGGER.debug("delete(categoryId:{})", categoryId);
        categoryRepository.deleteById(categoryId);
    }

}

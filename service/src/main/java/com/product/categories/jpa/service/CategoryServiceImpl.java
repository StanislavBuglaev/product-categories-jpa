package com.product.categories.jpa.service;

import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Integer categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Integer categoryId) {
        return categoryRepository.findById(categoryId).get();
    }

    @Override
    public void delete(Integer categoryId) {
         categoryRepository.deleteById(categoryId);
    }

    /*@Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public Optional<Category> findById(Integer categoryId) {
        return categoryDao.findById(categoryId);
    }

    @Override
    public Integer create(Category category) {
        return categoryDao.create(category);
    }

    @Override
    public Integer update(Category category) {
        return categoryDao.update(category);
    }

    @Override
    public Integer delete(Integer categoryId) {
        return categoryDao.delete(categoryId);
    }*/
}

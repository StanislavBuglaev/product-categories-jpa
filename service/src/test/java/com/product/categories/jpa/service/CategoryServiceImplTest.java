package com.product.categories.jpa.service;

import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findAll() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(new Category()));

        List<Category> categories = categoryService.findAll();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);
    }

    @Test
    void findById() {
        Integer categoryId = 1;
        String categoryName = "Test";
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));

        Optional<Category> optionalCategory = categoryService.findById(categoryId);
        assertTrue(optionalCategory.isPresent());
        assertSame(category, optionalCategory.get());
    }

    @Test
    void createOrUpdate() {
        Category category = new Category();
        when(categoryRepository.save(any())).thenReturn(category);

        category = categoryService.createOrUpdate(any());

        assertNotNull(category);

    }

    @Test
    void delete() {

    }
}
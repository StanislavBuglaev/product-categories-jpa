package com.product.categories.jpa.web_app;

import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Captor
    private ArgumentCaptor<Category> captor;

    @Test
    public void shouldReturnCategoriesPage() throws Exception {
        Category category1 = createCategory(1, "zoo");
        Category category2 = createCategory(2, "sports");
        when(categoryService.findAll()).thenReturn(Arrays.asList(category1, category2));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/categories")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("categories"));
    }

    @Test
    public void shouldOpenNewCategoryPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addCategory")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("addCategory"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("category", isA(Category.class)));
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    public void shouldAddNewCategory() throws Exception {
        String testName = "zoo";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/addCategory")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("categoryName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).createOrUpdate(captor.capture());

        Category category = captor.getValue();
        assertEquals(testName, category.getCategoryName());
    }

    @Test
    public void shouldOpenEditCategoryPageById() throws Exception {
        Category category1 = createCategory(1, "zoo");
        when(categoryService.findById(any())).thenReturn(Optional.of(category1));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/editCategory/" + category1.getCategoryId())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("editCategory"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("category", hasProperty("categoryId", is(category1.getCategoryId()))))
                .andExpect(model().attribute("category", hasProperty("categoryName", is(category1.getCategoryName()))));
    }

    @Test
    public void shouldReturnToCategoriesPageIfCategoryNotFoundById() throws Exception {
        int id = 99999;
        mockMvc.perform(
                MockMvcRequestBuilders.get("/editCategory/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/categories"));
        verify(categoryService).findById(id);
    }

    @Test
    public void shouldUpdateCategoryAfterEdit() throws Exception {

        String testName = "zoo";
        mockMvc.perform(
                MockMvcRequestBuilders.post("/editCategory/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("categoryId", "1")
                        .param("categoryName", testName)
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).createOrUpdate(captor.capture());

        Category category1 = captor.getValue();
        assertEquals(testName, category1.getCategoryName());
    }

    @Test
    public void shouldDeleteCategory() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/categories/3")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        verify(categoryService).delete(3);
    }

    private Category createCategory(int id, String name) {
        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryName(name);
        return category;
    }
}

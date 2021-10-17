package com.product.categories.jpa.rest_app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.categories.jpa.entity.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
class CategoryControllerIT {

    public static final String CATEGORIES_ENDPOINT = "/categories";

    @Autowired
    private CategoryController categoryController;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected MockCategoryService categoryService = new MockCategoryService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(categoryController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void categories() throws Exception {
        List<Category> categories = categoryService.findAll();
        assertNotNull(categories);
        assertTrue(true);
    }

    @Test
    void findById() throws Exception {
        List<Category> categories = categoryService.findAll();
        Assertions.assertNotNull(categories);
        assertTrue(true);

        Integer categoryId = categories.get(0).getCategoryId();
        Category expCategory = categoryService.findById(categoryId).get();
        Assertions.assertEquals(categoryId, expCategory.getCategoryId());
        Assertions.assertEquals(categories.get(0).getCategoryName(), expCategory.getCategoryName());
        Assertions.assertEquals(categories.get(0), expCategory);
    }

    @Test
    void createCategory() throws Exception {
       Category categoryTest =  categoryService.create(new Category());
       assertTrue(categoryTest.getCategoryId() == 1);
    }

    @Test
    void deleteCategory() throws Exception {
        Category categoryTest = categoryService.create(new Category());
        assertTrue(categoryTest.getCategoryId() == 1);
        categoryService.delete(categoryTest.getCategoryId());

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private class MockCategoryService {

        public List<Category> findAll() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(CATEGORIES_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Category>>() {});
        }

        public Optional<Category> findById(Integer categoryId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(CATEGORIES_ENDPOINT + "/" + categoryId)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Category.class));
        }

        public Category create(Category category) throws Exception {
            String json = objectMapper.writeValueAsString(category);
            MockHttpServletResponse response =
                    mockMvc.perform(post(CATEGORIES_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isCreated())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Category.class);
        }

        public void delete(Integer categoryId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.delete(new StringBuilder(CATEGORIES_ENDPOINT).append("/")
                            .append(categoryId).toString())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
        }
    }
}
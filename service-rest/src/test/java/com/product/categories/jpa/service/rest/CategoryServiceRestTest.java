package com.product.categories.jpa.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.service.CategoryService;
import com.product.categories.jpa.service.rest.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.product.categories.jpa.service.rest.config.TestConfig.CATEGORIES_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class CategoryServiceRestTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceRestTest.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CategoryService categoryService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFindAllCategories() throws Exception {

        LOGGER.debug("shouldFindAllCategories()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createCategory(0),
                                createCategory(1))))
                );

        // when
        List<Category> categories = categoryService.findAll();

        // then
        mockServer.verify();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);
    }

    @Test
    public void shouldCreateCategory() throws Exception {

        LOGGER.debug("shouldCreateCategory()");
        // given
        Category category = new Category();
        category.setCategoryName("Test name");

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(category))
                );
        // when
        Category categoryResult = categoryService.create(category);

        // then
        mockServer.verify();
        assertNotNull(categoryResult);
    }

    @Test
    public void shouldFindCategoryById() throws Exception {

        // given
        Integer id = 1;
        Category category = new Category();
        category.setCategoryName("Random");
        category.setCategoryId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(category))
                );

        // when
        Optional<Category> optionalCategory = categoryService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalCategory.isPresent());
        assertEquals(optionalCategory.get().getCategoryId(), id);
        assertEquals(optionalCategory.get().getCategoryName(), category.getCategoryName());
    }

    @Test
    public void shouldUpdateCategory() throws Exception {

        // given
        Integer id = 1;
        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryName("Random2");

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(category))
                );

        // when
        Category result = categoryService.create(category);
        Optional<Category> updatedCategoryOptional = categoryService.findById(id);

        // then
        mockServer.verify();
        assertTrue(1 == result.getCategoryId());

        assertTrue(updatedCategoryOptional.isPresent());
        assertEquals(updatedCategoryOptional.get().getCategoryId(), id);
        assertEquals(updatedCategoryOptional.get().getCategoryName(), category.getCategoryName());
    }

    @Test
    public void shouldDeleteCategory() throws Exception {

        // given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        categoryService.delete(id);

        // then
        mockServer.verify();
    }

    private Category createCategory(int index) {
        Category category = new Category();
        category.setCategoryId(index);
        category.setCategoryName("category" + index);
        return category;
    }
}

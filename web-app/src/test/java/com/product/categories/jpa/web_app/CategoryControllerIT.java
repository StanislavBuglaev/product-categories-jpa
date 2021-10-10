package com.product.categories.jpa.web_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.categories.jpa.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
class CategoryControllerIT {

    private static final String CATEGORIES_URL = "http://localhost:8088/categories";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldReturnCategoriesPage() throws Exception {
        Category category1 = createCategory(1, "zoo");
        Category category2 = createCategory(2, "household");
        Category category3 = createCategory(3, "sports");

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(category1, category2, category3)))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/categories")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("categories"));

        mockServer.verify();
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
        mockServer.verify();
    }

    @Test
    public void shouldAddNewCategory() throws Exception {

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/addCategory")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("categoryName", "test")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        mockServer.verify();
    }

    @Test
    public void shouldOpenEditCategoryPageById() throws Exception {
        Category category = createCategory(1, "zoo");
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + category.getCategoryId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(category))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/editCategory/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("editCategory"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("category", hasProperty("categoryId", is(category.getCategoryId()))))
                .andExpect(model().attribute("category", hasProperty("categoryName", is(category.getCategoryName()))));
        mockServer.verify();
    }

    @Test
    public void shouldReturnToCategoriesPageIfCategoryNotFoundById() throws Exception {
        int id = 99999;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/editCategory/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/categories"));
        mockServer.verify();
    }

    @Test
    public void shouldUpdateCategoryAfterEdit() throws Exception {

    }

    @Test
    public void shouldDeleteCategory() throws Exception {
        int id = 3;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(CATEGORIES_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/categories/3")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        mockServer.verify();
    }

    private Category createCategory(int id, String name) {
        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryName(name);
        return category;
    }
}
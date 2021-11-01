package com.product.categories.jpa.web_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.entity.Product;
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

import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ProductControllerIT {

    private static final String PRODUCTS_URL = "http://localhost:8088/products";

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
    public void shouldReturnProductsPage() throws Exception {

        Product product1 = createProduct(1, "cat food", 400.0);
        Product product2 = createProduct(2, "dog food", 400.0);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(product1, product2)))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("products"));

        mockServer.verify();
    }

    @Test
    public void shouldOpenNewProductPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addProduct")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("addProduct"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("product", isA(Category.class)));
        mockServer.verify();
    }

    @Test
    public void shouldAddNewProduct() throws Exception {

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/addProduct/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("productName", "test")
                        .param("productPrice", String.valueOf(400.0))
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        mockServer.verify();
    }

    @Test
    public void shouldOpenEditProductPageById() throws Exception {
        Product product = createProduct(1, "cat food", 400.0);
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL + "/" + product.getProductId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(product))
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/editProduct/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("editCategory"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("product", hasProperty("productId", is(product.getProductId()))))
                .andExpect(model().attribute("product", hasProperty("productName", is(product.getProductName()))))
                .andExpect(model().attribute("product", hasProperty("productPrice", is(product.getProductPrice()))));
        mockServer.verify();
    }

    @Test
    public void shouldReturnToProductsPageIfProductNotFoundById() throws Exception {
        int id = 99999;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/editProduct/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products"));
        mockServer.verify();
    }

    @Test
    public void shouldUpdateProductAfterEdit() throws Exception {

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        String testName = "cat food";
        Double testPrice = 450.0;
        Category categoryTest = createCategory(1, "zoo");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/editProduct/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("productId", "1")
                        .param("productName", testName)
                        .param("productPrice", String.valueOf(testPrice))
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/products"))
                .andExpect(redirectedUrl("/products"));

        mockServer.verify();
    }

    @Test
    public void shouldDeleteCategory() throws Exception {
        int id = 3;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products/3")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/products"))
                .andExpect(redirectedUrl("/products"));

        mockServer.verify();
    }

    private Category createCategory(int id, String name) {
        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryName(name);
        return category;
    }

    private Product createProduct(int id, String name, double price) {
        Product product = new Product();
        product.setProductId(id);
        product.setProductName(name);
        product.setProductPrice(price);
        product.setCategory(createCategory(1, "zoo"));
        return product;
    }
}
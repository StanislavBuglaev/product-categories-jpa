package com.product.categories.jpa.web_app;

import com.product.categories.jpa.entity.Category;
import com.product.categories.jpa.entity.Product;
import com.product.categories.jpa.service.ProductService;
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

import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Captor
    private ArgumentCaptor<Product> captor;

    @Test
    public void shouldReturnProductsPage() throws Exception {
        Product product1 = createProduct(1, "cat food", 400.0);
        Product product2 = createProduct(2, "football ball", 750.0);
        when(productService.findAll()).thenReturn(Arrays.asList(product1, product2));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("products"));
    }

    @Test
    public void shouldOpenNewProductPage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addProduct/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("addProduct"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("product", isA(Product.class)));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldAddNewProduct() throws Exception {
        String testName = "cat food";
        double testPrice = 400.0;
        mockMvc.perform(
                MockMvcRequestBuilders.post("/addProduct/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("productName", testName)
                        .param("productPrice", String.valueOf(testPrice))
                        .param("productCategory", String.valueOf(createCategory(1, "zoo")))
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/categories"))
                .andExpect(redirectedUrl("/categories"));

        verify(productService).createOrUpdate(captor.capture());

        Product product = captor.getValue();
        assertEquals(testName, product.getProductName());
        assertEquals(testPrice, product.getProductPrice());
    }

    @Test
    public void shouldOpenEditProductPageById() throws Exception {
        Product product1 = createProduct(1, "cat food", 400.0);
        when(productService.findById(any())).thenReturn(Optional.of(product1));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/editProduct/" + product1.getProductId())
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("editProduct"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(model().attribute("product", hasProperty("productId", is(product1.getProductId()))))
                .andExpect(model().attribute("product", hasProperty("productName", is(product1.getProductName()))))
                .andExpect(model().attribute("product", hasProperty("productPrice", is(product1.getProductPrice()))));
    }

    @Test
    public void shouldReturnToProductsPageIfProductNotFoundById() throws Exception {
        int id = 99999;
        mockMvc.perform(
                MockMvcRequestBuilders.get("/editProduct/" + id)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/products"));
        verify(productService).findById(id);
    }

    @Test
    public void shouldUpdateCategoryAfterEdit() throws Exception {

        String testName = "cat food";
        double testPrice = 400.0;

        mockMvc.perform(
                MockMvcRequestBuilders.post("/editProduct/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("productId", "1")
                        .param("productName", testName)
                        .param("productPrice", String.valueOf(testPrice))
                        .param("productCategory", String.valueOf(createCategory(1, "zoo")))
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/products"))
                .andExpect(redirectedUrl("/products"));

        verify(productService).createOrUpdate(captor.capture());

        Product product1 = captor.getValue();
        assertEquals(testName, product1.getProductName());
        assertEquals(testPrice, product1.getProductPrice());
    }

    @Test
    public void shouldDeleteProduct() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/products/3")
        ).andExpect(status().isFound())
                .andExpect(view().name("redirect:/products"))
                .andExpect(redirectedUrl("/products"));

        verify(productService).delete(3);
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

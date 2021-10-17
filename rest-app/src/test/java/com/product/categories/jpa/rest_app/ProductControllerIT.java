package com.product.categories.jpa.rest_app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.categories.jpa.entity.Product;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

    public static final String PRODUCTS_ENDPOINT = "/products";

    @Autowired
    private ProductController productController;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected ProductControllerIT.MockProductService productService = new ProductControllerIT.MockProductService();

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(productController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void products() throws Exception {
        List<Product> products = productService.findAll();
        assertNotNull(products);
        assertTrue(true);
    }

    @Test
    void findById() throws Exception {
        List<Product> products = productService.findAll();
        Assertions.assertNotNull(products);
        assertTrue(true);

        Integer productId = products.get(0).getProductId();
        Product expProduct = productService.findById(productId).get();
        Assertions.assertEquals(productId, expProduct.getProductId());
        Assertions.assertEquals(products.get(0).getProductName(), expProduct.getProductName());
        Assertions.assertEquals(products.get(0), expProduct);
    }

    @Test
    void createProduct() throws Exception {
        Product productTest =  productService.create(new Product());
        assertTrue(productTest.getProductId() == 1);
    }

    @Test
    void deleteProduct() throws Exception {
        Product productTest = productService.create(new Product());
        assertTrue(productTest.getProductId() == 1);
        productService.delete(productTest.getProductId());

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private class MockProductService {

        public List<Product> findAll() throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(PRODUCTS_ENDPOINT)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            assertNotNull(response);

            return objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Product>>() {});
        }

        public Optional<Product> findById(Integer productId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(get(PRODUCTS_ENDPOINT + "/" + productId)
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
            return Optional.of(objectMapper.readValue(response.getContentAsString(), Product.class));
        }

        public Product create(Product product) throws Exception {
            String json = objectMapper.writeValueAsString(product);
            MockHttpServletResponse response =
                    mockMvc.perform(post(PRODUCTS_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isCreated())
                            .andReturn().getResponse();
            return objectMapper.readValue(response.getContentAsString(), Product.class);
        }

        public void delete(Integer productId) throws Exception {
            MockHttpServletResponse response = mockMvc.perform(
                    MockMvcRequestBuilders.delete(new StringBuilder(PRODUCTS_ENDPOINT).append("/")
                            .append(productId).toString())
                            .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
                    .andReturn().getResponse();
        }
    }
}

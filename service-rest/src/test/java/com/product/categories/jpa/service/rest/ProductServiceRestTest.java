package com.product.categories.jpa.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.categories.jpa.entity.Product;
import com.product.categories.jpa.service.ProductService;
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

import static com.product.categories.jpa.service.rest.config.TestConfig.PRODUCTS_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class ProductServiceRestTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceRestTest.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProductService productService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFindAllProducts() throws Exception {

        LOGGER.debug("shouldFindAllProducts()");
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createProduct(0),
                                createProduct(1))))
                );

        // when
        List<Product> products = productService.findAll();

        // then
        mockServer.verify();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    public void shouldCreateProduct() throws Exception {

        LOGGER.debug("shouldCreateProduct()");
        // given
        Product product = new Product();
        product.setProductName("Test name");
        product.setProductPrice(200.7);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(product))
                );
        // when
        Product productResult = productService.createOrUpdate(product);

        // then
        mockServer.verify();
        assertNotNull(productResult);
    }

    @Test
    public void shouldFindProductById() throws Exception {

        // given
        Integer id = 1;
        Product product = new Product();
        product.setProductPrice(200.5);
        product.setProductName("Random");
        product.setProductId(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(product))
                );

        // when
        Optional<Product> optionalProduct = productService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalProduct.isPresent());
        assertEquals(optionalProduct.get().getProductId(), id);
        assertEquals(optionalProduct.get().getProductName(), product.getProductName());
    }

    @Test
    public void shouldUpdateProduct() throws Exception {

        // given
        Integer id = 1;
        Product product = new Product();
        product.setProductId(id);
        product.setProductName("Random82");
        product.setProductPrice(200.6);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(product))
                );

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(product))
                );

        // when
        Product result = productService.createOrUpdate(product);
        Optional<Product> updatedProductOptional = productService.findById(id);

        // then
        mockServer.verify();
        assertTrue(1 == result.getProductId());

        assertTrue(updatedProductOptional.isPresent());
        assertEquals(updatedProductOptional.get().getProductId(), id);
        assertEquals(updatedProductOptional.get().getProductName(), product.getProductName());
    }

    @Test
    public void shouldDeleteProduct() throws Exception {

        // given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PRODUCTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );
        // when
        productService.delete(id);

        // then
        mockServer.verify();
    }

    private Product createProduct(int index) {
        Product product = new Product();
        product.setProductId(index);
        product.setProductName("product" + index);
        product.setProductPrice(200.0);
        return product;
    }
}

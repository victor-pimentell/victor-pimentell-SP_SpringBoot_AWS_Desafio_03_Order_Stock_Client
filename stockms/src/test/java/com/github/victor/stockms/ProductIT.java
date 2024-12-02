package com.github.victor.stockms;

import com.github.victor.stockms.entities.Product;
import com.github.victor.stockms.web.dto.ProductCreateDto;
import com.github.victor.stockms.web.dto.ProductNameDto;
import com.github.victor.stockms.web.dto.ProductQuantityDto;
import com.github.victor.stockms.web.dto.ProductResponseDto;
import com.github.victor.stockms.web.exceptions.handler.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql-product/InsertDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql-product/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProductIT {

    @Autowired
    WebTestClient testClient;

    @Test
    @Sql(scripts = "/sql-product/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createProduct_WithValidArgs_ReturnStatus201() {
        ProductResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/stock/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductCreateDto("Controle", 2))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(ProductResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Controle");
        org.assertj.core.api.Assertions.assertThat(responseBody.getQuantity()).isEqualTo(2);
    }

    @Test
    @Sql(scripts = "/sql-product/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql-product/InsertDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createProduct_WithRepeatedName_ReturnStatus409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/stock/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductCreateDto("Product A", 10))
                .exchange()
                .expectStatus()
                .isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    @Sql(scripts = "/sql-product/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createProduct_WithInvalidName_ReturnStatus400() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/stock/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductCreateDto("", 1))
                .exchange()
                .expectStatus()
                .isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    @Sql(scripts = "/sql-product/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createProduct_WithInvalidQuantity_ReturnStatus400() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/stock/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductCreateDto("product", null))
                .exchange()
                .expectStatus()
                .isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .post()
                .uri("/api/v1/stock/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductCreateDto("Ash", 0))
                .exchange()
                .expectStatus()
                .isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void updateProductsQuantities_WithvalidArgs_ReturnStatus204() {
        List<Product> list = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setQuantity(15);
        product.setHash("a04bd35be16789649a70cf2e6712d8e76ffbf2d993ad23db84c6e12539150e8d");
        list.add(product);

        testClient
                .put()
                .uri("/api/v1/stock/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(list)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    public void updateProductsQuantities_WithInvalidHash_ReturnStatus204() {
        List<Product> list = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");
        product.setQuantity(15);
        product.setHash("a04bd35b6789649a70cf2e6712d8e76ffbf2d993ad23db84c6e12539150e8d");
        list.add(product);

        ErrorMessage responseBody = testClient
                .put()
                .uri("/api/v1/stock/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(list)
                .exchange()
                .expectStatus()
                .isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void getClientById_WithValidId_ReturnStatus200() {
        ProductResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/stock/products/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ProductResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Product A");
    }

    @Test
    public void getClientById_WithInvalidId_ReturnStatus404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/stock/products/0")
                .exchange()
                .expectStatus()
                .isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    @Sql(scripts = "/sql-product/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql-product/InsertDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllProducts_WithPagination_ReturnsStatus200() {
        int expectedPage = 0;
        int expectedLimit = 2;

        testClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/stock/products")
                        .queryParam("page", expectedPage)
                        .queryParam("limit", expectedLimit)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content.length()").isEqualTo(expectedLimit)
                .jsonPath("$.pageable.pageNumber").isEqualTo(expectedPage)
                .jsonPath("$.size").isEqualTo(expectedLimit);
    }

    @Test
    @Sql(scripts = "/sql-product/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql-product/InsertDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAllProducts_WithPagination_ReturnsPagedResults() {
        int expectedPage = 0;
        int expectedLimit = 2;

        testClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/stock/products")
                        .queryParam("page", expectedPage)
                        .queryParam("limit", expectedLimit)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content.length()").isEqualTo(expectedLimit)
                .jsonPath("$.pageable.pageNumber").isEqualTo(expectedPage)
                .jsonPath("$.size").isEqualTo(expectedLimit);
    }

    @Test
    @Sql(scripts = "/sql-product/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql-product/InsertDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateQuantity_WithValidArgs_ReturnsUpdatedProduct() {
        ProductQuantityDto updateRequest = new ProductQuantityDto(1L, 15);

        ProductResponseDto responseBody = testClient
                .patch()
                .uri("/api/v1/stock/products/quantity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ProductResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(updateRequest.getId());
        org.assertj.core.api.Assertions.assertThat(responseBody.getQuantity()).isEqualTo(updateRequest.getQuantity());
    }

    @Test
    public void updateName_WithValidArgs_ReturnsUpdatedProduct() {
        ProductNameDto updateRequest = new ProductNameDto(1L, "Produto Atualizado");

        ProductResponseDto responseBody = testClient
                .patch()
                .uri("/api/v1/stock/products/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ProductResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(updateRequest.getId());
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo(updateRequest.getName());
    }

    @Test
    public void deleteProductById_WithValidId_ReturnsNoContent() {

        testClient.delete()
                .uri("/api/v1/stock/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}
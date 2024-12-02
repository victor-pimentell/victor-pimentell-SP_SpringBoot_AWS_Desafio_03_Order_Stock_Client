package com.github.victor.orderms;

import com.github.victor.orderms.web.dto.OrderResponseDto;
import com.github.victor.orderms.web.dto.UpdateEmailDto;
import com.github.victor.orderms.web.exceptions.handler.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql-order/InsertDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql-order/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void getClientById_WithValidId_ReturnStatus200() {
        OrderResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/orders/id/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(OrderResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getEmail()).isEqualTo("user1@example.com");
    }

    @Test
    public void getClientById_WithInvalidId_ReturnStatus404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/orders/id/0")
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
    public void getOrdersByEmail_WithValidEmail_ReturnsStatus200() {

        List<OrderResponseDto> responseBody = testClient.get()
                .uri("/api/v1/orders/email/user1@example.com")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(OrderResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotEmpty();
        org.assertj.core.api.Assertions.assertThat(responseBody.get(0).getEmail()).isEqualTo("user1@example.com");
    }

    @Test
    public void getOrdersByEmail_WithInvalidEmail_ReturnsOrdersList() {

        List<OrderResponseDto> responseBody = testClient.get()
                .uri("/api/v1/orders/email/user@example.com")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(OrderResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody).isEmpty();
    }

    @Test
    public void updateOrderEmail_WithValidData_ReturnsStatus204() {
        testClient.put()
                .uri("/api/v1/orders/email")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateEmailDto("user1@example.com", "updated@example.com"))
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}

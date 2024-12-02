package com.github.victor.clientms;

import com.github.victor.clientms.web.dto.ClientCreateDto;
import com.github.victor.clientms.web.dto.ClientResponseDto;
import com.github.victor.clientms.web.dto.ClientUpdateName;
import com.github.victor.clientms.web.exceptions.handler.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql-client/InsertDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql-client/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIT {

    @Autowired
    WebTestClient testClient;


    @Test
    @Sql(scripts = "/sql-client/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createClient_WithValidArgs_ReturnStatus201() {
        ClientResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientCreateDto("victor", "victor@example.com"))
                        .exchange()
                        .expectStatus()
                        .isCreated()
                        .expectBody(ClientResponseDto.class)
                        .returnResult()
                        .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("victor");
        org.assertj.core.api.Assertions.assertThat(responseBody.getEmail()).isEqualTo("victor@example.com");
    }

    @Test
    public void createClient_WithRepeatedEmail_ReturnStatus409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientCreateDto("Ash", "john.doe@example.com"))
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
    @Sql(scripts = "/sql-client/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createClient_WithInvalidEmail_ReturnStatus400() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientCreateDto("Ash", ""))
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
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientCreateDto("Ash", "test"))
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
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientCreateDto("Ash", "@example.com"))
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
    public void createClient_WithInvalidName_ReturnStatus400() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientCreateDto("", "ash@example.com"))
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
    public void getClientById_WithValidId_ReturnStatus200() {
        ClientResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/clients/id/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ClientResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("John Doe");
        org.assertj.core.api.Assertions.assertThat(responseBody.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void getClientById_WithInvalidId_ReturnStatus404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/clients/id/1000")
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
    public void getClientByEmail_WithValidEmail_ReturnStatus200() {
        ClientResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/clients/email/john.doe@example.com")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ClientResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("John Doe");
        org.assertj.core.api.Assertions.assertThat(responseBody.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void getClientByEmail_WithInvalidEmail_ReturnStatus404() {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/clients/email/invalid@example.com")
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
    public void nameUpdate_WithValidArgs_ReturnStatus201() {
        ClientResponseDto responseBody = testClient
                .patch()
                .uri("/api/v1/clients/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientUpdateName("Ash", "john.doe@example.com"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ClientResponseDto.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Ash");
        org.assertj.core.api.Assertions.assertThat(responseBody.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void nameUpdate_WithInvalidArgs_ReturnStatus400() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("/api/v1/clients/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientUpdateName("", "john.doe@example.com"))
                .exchange()
                .expectStatus()
                .isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .patch()
                .uri("/api/v1/clients/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientUpdateName("Ash", "@example.com"))
                .exchange()
                .expectStatus()
                .isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .patch()
                .uri("/api/v1/clients/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientUpdateName("Ash", "ash"))
                .exchange()
                .expectStatus()
                .isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .patch()
                .uri("/api/v1/clients/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientUpdateName("Ash", ""))
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
    @Sql(scripts = "/sql-client/InsertDataInSQL.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql-client/DeleteDataInSQL.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteProductById_WithValidId_ReturnStatus204() {
        testClient
                .delete()
                .uri("/api/v1/clients/1")
                .exchange()
                .expectStatus()
                .isNoContent();
    }


    @Test
    public void deleteProductById_WithInvalidId_ReturnStatus404() {
        testClient
                .delete()
                .uri("/api/v1/clients/0")
                .exchange()
                .expectStatus()
                .isEqualTo(204);
    }
}

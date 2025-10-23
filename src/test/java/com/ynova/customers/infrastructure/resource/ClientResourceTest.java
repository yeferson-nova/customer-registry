package com.ynova.customers.infrastructure.resource;

import com.ynova.customers.application.dto.CreateClientRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class ClientResourceTest {

    private String userToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        userToken = generateToken("user");
        adminToken = generateToken("admin");
    }

    private String generateToken(String... roles) {
        return Jwt.issuer("https://your-auth-server.com")
                .upn("testuser@example.com")
                .groups(new HashSet<>(Arrays.asList(roles)))
                .expiresIn(Duration.ofMinutes(10))
                .sign();
    }

    @Test
    void createClient_whenValidRequest_shouldReturn201() {
        CreateClientRequest request = new CreateClientRequest(
                "Valid Client",
                LocalDate.of(1990, 1, 1),
                1L,
                "123456789012",
                1L
        );

        given()
                .auth().oauth2(userToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/clientes")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is("Valid Client"))
                .body("status", is("ACTIVE"));
    }

    @Test
    void createClient_whenChileValidationFails_shouldReturn400() {
        CreateClientRequest request = new CreateClientRequest(
                "Chilean Client",
                LocalDate.of(1992, 5, 10),
                2L,
                "987654321098",
                2L
        );

        given()
                .auth().oauth2(userToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/clientes")
                .then()
                .statusCode(400)
                .body("error", is("Invalid account number for Chile. It must start with '003'."));
    }

    @Test
    void createClient_whenChileValidationSucceeds_shouldReturn201() {
        CreateClientRequest request = new CreateClientRequest(
                "Valid Chilean Client",
                LocalDate.of(1993, 6, 11),
                1L,
                "003987654321",
                2L
        );

        given()
                .auth().oauth2(userToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/clientes")
                .then()
                .statusCode(201)
                .body("country", is("CHILE"));
    }

    @Test
    void createClient_whenNoToken_shouldReturn401() {
        CreateClientRequest request = new CreateClientRequest("No Auth Client", LocalDate.now(), 1L, "123", 1L);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/clientes")
                .then()
                .statusCode(401);
    }

    @Test
    void inactivateClient_whenUserRole_shouldReturn403() {
        given()
                .auth().oauth2(userToken)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/clientes/1/inactivate")
                .then()
                .statusCode(403);
    }

    @Test
    void inactivateClient_whenAdminRole_shouldReturn204() {
        CreateClientRequest request = new CreateClientRequest("To Inactivate", LocalDate.of(2000, 1, 1), 1L, "555555555555", 1L);
        Integer clientId = given()
                .auth().oauth2(userToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/api/v1/clientes").then().extract().path("id");

        given()
                .auth().oauth2(adminToken)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/clientes/" + clientId + "/inactivate")
                .then()
                .statusCode(204);
    }
}

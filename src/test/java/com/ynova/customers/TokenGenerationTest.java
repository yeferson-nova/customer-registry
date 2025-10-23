package com.ynova.customers;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

@QuarkusTest
public class TokenGenerationTest {

    @Test
    void generateUserToken() {
        String token = Jwt.issuer("https://your-auth-server.com")
                .upn("testuser@example.com")
                .groups(new HashSet<>(Arrays.asList("user")))
                .subject("test-user-123")
                .expiresIn(Duration.ofHours(1))
                .sign();

        System.out.println("===== COPIA Y PEGA ESTE TOKEN DE USUARIO EN POSTMAN =====");
        System.out.println(token);
        System.out.println("=========================================================");
    }

    @Test
    void generateAdminToken() {
        String token = Jwt.issuer("https://your-auth-server.com")
                .upn("testadmin@example.com")
                .groups(new HashSet<>(Arrays.asList("admin")))
                .subject("test-admin-456")
                .expiresIn(Duration.ofHours(1))
                .sign();

        System.out.println("===== COPIA Y PEGA ESTE TOKEN DE ADMIN EN POSTMAN =====");
        System.out.println(token);
        System.out.println("========================================================");
    }
}

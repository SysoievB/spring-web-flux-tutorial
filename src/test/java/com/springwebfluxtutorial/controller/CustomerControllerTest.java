package com.springwebfluxtutorial.controller;

import com.springwebfluxtutorial.entities.Customer;
import com.springwebfluxtutorial.service.CustomerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
class CustomerControllerTest {

    private WebClient webClient = WebClient.create("http://localhost:8080");

    @Autowired
    private CustomerService serviceUnderTest;

    @BeforeAll
    Flux<Customer> setUp() {
        return Flux.just(
                new Customer(1L, "Mereal", 26, "Tutor"),
                new Customer(2L, "Blexa", 28, "HR"),
                new Customer(3L, "Anton", 20, "Developer")
        );
    }

    @AfterAll
    void tearDown(){

    }

    @Test
    void itShouldGetAll() {
        // Given
        Flux<Customer> customerFlux = webClient.get()
                .uri("/api/v1/customer")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchangeToFlux(response -> {
                    assertEquals(HttpStatus.OK, response.statusCode());
                    return response.bodyToFlux(Customer.class);
                });

        // When
        // Then
        StepVerifier.create(customerFlux)
                .expectSubscription()
                .thenConsumeWhile(customer -> true,
                        customer -> {
                            assertNotNull(customer.getId());
                            assertNotNull(customer.getName());
                            assertNotNull(customer.getAge());
                            assertNotNull(customer.getJob());
                        })
                .verifyComplete();
    }


    @Test
    void itShouldGetById() {
        // Given
        // When
        // Then
    }

    @Test
    void itShouldGetByName() {
        // Given
        // When
        // Then
    }

    @Test
    void itShouldDelete() {
        // Given
        // When
        // Then
    }

    @Test
    void itShouldUpdate() {
        // Given
        // When
        // Then
    }

    @Test
    void itShouldSave() {
        // Given
        // When
        // Then
    }
}
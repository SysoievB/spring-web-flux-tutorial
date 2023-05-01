package com.springwebfluxtutorial.controller;

import com.springwebfluxtutorial.entities.Customer;
import com.springwebfluxtutorial.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
class CustomerControllerTest {
    @Mock
    private CustomerService serviceUnderTest;
    @InjectMocks
    private CustomerController controllerUnderTest;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "Mereal", 26, "Tutor");
    }

    @AfterEach
    void tearDown() {
        customer = null;
    }

    @Test
    void itShouldGetAll() {
        // Given
        given(serviceUnderTest.getAllCustomers()).willReturn(Flux.just(customer));

        // When
        Flux<Customer> customerFlux = controllerUnderTest.getAll();

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

        verify(serviceUnderTest, atLeastOnce()).getAllCustomers();
    }

    @Test
    void itShouldGetById() {
        // Given
        when(serviceUnderTest.getCustomerById(customer.getId())).thenReturn(Mono.just(customer));

        // When
        Mono<ResponseEntity<Customer>> result = controllerUnderTest.getById(customer.getId());

        // Then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNext(ResponseEntity.ok(customer))
                .verifyComplete();

        verify(serviceUnderTest, atLeastOnce()).getCustomerById(customer.getId());
    }


    @Test
    void itShouldGetByName() {
        // Given
        when(serviceUnderTest.findCustomerByName(customer.getName())).thenReturn(Mono.just(customer));

        // When
        Mono<ResponseEntity<Customer>> result = controllerUnderTest.getByName(customer.getName());

        // Then
        StepVerifier.create(result)
                .expectSubscription()
                .expectNext(ResponseEntity.ok(customer))
                .verifyComplete();

        verify(serviceUnderTest, atLeastOnce()).findCustomerByName(customer.getName());
    }

    @Test
    void itShouldDelete() {
        // Given
        given(serviceUnderTest.deleteCustomerById(customer.getId())).willReturn(Mono.empty());

        // When
        Mono<Void> result = serviceUnderTest.deleteCustomerById(customer.getId());

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(serviceUnderTest, atLeastOnce()).deleteCustomerById(customer.getId());
    }

    @Test
    void itShouldUpdate() {
        // Given
        Customer customerToUpdate = new Customer(1L, "John", 34, "HR Manager");
        given(serviceUnderTest.getCustomerById(customer.getId())).willReturn(Mono.just(customer));
        given(serviceUnderTest.createCustomer(customer)).willReturn(Mono.just(customerToUpdate));

        // When
        Mono<Customer> customerMono = serviceUnderTest.updateCustomer(customer.getId(), customerToUpdate);

        // Then
        StepVerifier.create(customerMono)
                .assertNext(updateCustomer -> {
                    assertNotNull(updateCustomer.getId());
                    assertEquals(updateCustomer.getName(), customer.getName());
                    assertEquals(updateCustomer.getAge(), customer.getAge());
                    assertEquals(updateCustomer.getJob(), customer.getJob());
                })
                .expectComplete()
                .verify();

        assertAll(
                () -> assertNotNull(customerMono.block()),
                () -> assertEquals(customerToUpdate.getId(), Objects.requireNonNull(customerMono.block()).getId()),
                () -> assertEquals(customerToUpdate.getName(), Objects.requireNonNull(customerMono.block()).getName()),
                () -> assertEquals(customerToUpdate.getAge(), Objects.requireNonNull(customerMono.block()).getAge()),
                () -> assertEquals(customerToUpdate.getJob(), Objects.requireNonNull(customerMono.block()).getJob())
        );
        verify(serviceUnderTest, atLeastOnce()).getCustomerById(customer.getId());
        verify(serviceUnderTest, atLeastOnce()).createCustomer(customerToUpdate);
    }

    @Test
    void itShouldSave() {
        // Given
        given(serviceUnderTest.createCustomer(customer)).willReturn(Mono.just(customer));

        // When
        Mono<Customer> createdCustomer = controllerUnderTest.save(customer);

        // Then
        StepVerifier.create(createdCustomer)
                .assertNext(savedCustomer -> {
                    assertNotNull(savedCustomer.getId());
                    assertEquals(savedCustomer.getName(), customer.getName());
                    assertEquals(savedCustomer.getAge(), customer.getAge());
                    assertEquals(savedCustomer.getJob(), customer.getJob());
                })
                .expectComplete()
                .verify();

        verify(serviceUnderTest, atLeastOnce()).createCustomer(customer);
    }
}

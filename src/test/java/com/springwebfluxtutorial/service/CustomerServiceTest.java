package com.springwebfluxtutorial.service;

import com.springwebfluxtutorial.entities.Customer;
import com.springwebfluxtutorial.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository repositoryUnderTest;
    @InjectMocks
    private CustomerService serviceUnderTest;

    @Test
    void itShouldGetAllCustomers() {
        // Given
        given(repositoryUnderTest.findAll()).willReturn(Flux.just(new Customer(), new Customer(), new Customer()));

        // When
        Flux<Customer> customers = serviceUnderTest.getAllCustomers();

        // Then
        assertTrue(Objects.requireNonNull(customers.collectList().block()).size() > 0);
        verify(repositoryUnderTest, atLeastOnce()).findAll();
    }

    @Test
    void itShouldGetCustomerById() {
        // Given
        given(repositoryUnderTest.findById(anyLong())).willReturn(Mono.just(new Customer()));

        // When
        Mono<Customer> customer = serviceUnderTest.getCustomerById(anyLong());

        // Then
        assertNotNull(customer.block());
        verify(repositoryUnderTest, atLeastOnce()).findById(anyLong());
    }

    @Test
    void itShouldDeleteCustomerById() {
        // Given
        given(repositoryUnderTest.deleteById(anyLong())).willReturn(Mono.empty());

        // When
        serviceUnderTest.deleteCustomerById(anyLong());

        // Then
        verify(repositoryUnderTest, atLeastOnce()).deleteById(anyLong());
    }

    @Test
    void itShouldCreateCustomer() {
        // Given
        Customer customerToSave = new Customer(1L, "John", 34, "HR Manager");
        given(repositoryUnderTest.save(any(Customer.class))).willReturn(Mono.just(customerToSave));

        // When
        Mono<Customer> createdCustomer = serviceUnderTest.createCustomer(customerToSave);

        // Then
        assertNotNull(createdCustomer.block());
        verify(repositoryUnderTest, atLeastOnce()).save(any(Customer.class));
    }

    @Test
    void itShouldUpdateCustomer() {
        // Given
        Customer customer = new Customer(1L, "Ann", 20, "Recruiter");
        Customer customerToUpdate = new Customer(1L, "John", 34, "HR Manager");
        given(repositoryUnderTest.findById(customer.getId())).willReturn(Mono.just(customer));
        given(repositoryUnderTest.save(customer)).willReturn(Mono.just(customerToUpdate));

        // When
        Mono<Customer> customerMono = serviceUnderTest.updateCustomer(customer.getId(), customerToUpdate);

        // Then
        assertAll(
                () -> assertNotNull(customerMono.block()),
                () -> assertEquals(customerToUpdate.getId(), Objects.requireNonNull(customerMono.block()).getId()),
                () -> assertEquals(customerToUpdate.getName(), Objects.requireNonNull(customerMono.block()).getName()),
                () -> assertEquals(customerToUpdate.getAge(), Objects.requireNonNull(customerMono.block()).getAge()),
                () -> assertEquals(customerToUpdate.getJob(), Objects.requireNonNull(customerMono.block()).getJob())
        );
        verify(repositoryUnderTest, atLeastOnce()).findById(customer.getId());
        verify(repositoryUnderTest, atLeastOnce()).save(customerToUpdate);
    }

    @Test
    void itShouldFindCustomerByName() {
        // Given
        given(repositoryUnderTest.getCustomerByName(anyString())).willReturn(Mono.just(new Customer()));

        // When
        Mono<Customer> customer = serviceUnderTest.findCustomerByName(anyString());

        // Then
        assertNotNull(customer.block());
        verify(repositoryUnderTest, atLeastOnce()).getCustomerByName(anyString());
    }
}

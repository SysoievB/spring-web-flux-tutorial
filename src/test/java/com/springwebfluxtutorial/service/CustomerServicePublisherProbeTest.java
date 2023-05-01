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
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CustomerServicePublisherProbeTest {
    @Mock
    private CustomerRepository underTestRepository;
    @InjectMocks
    private CustomerService underTestService;
    private Customer customer = new Customer(1L, "testName", 33, "testJob");

    @Test
    void itShouldReturnAllCustomers() {
        //Given
        PublisherProbe<Customer> probe = PublisherProbe.of(
                Flux.just(customer, new Customer(), new Customer())
        );
        given(underTestRepository.findAll()).willReturn(probe.flux());

        //When
        Flux<Customer> customers = underTestService.getAllCustomers();

        //Then
        StepVerifier.create(customers)
                .expectNextMatches(element -> {
                    assertThat(element)
                            .returns(1L, Customer::getId)
                            .returns("testName", Customer::getName)
                            .returns(33, Customer::getAge)
                            .returns("testJob", Customer::getJob);
                    return true;
                })
                .expectNextCount(2L)
                .verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
        assertEquals(1, probe.subscribeCount());
    }

    @Test
    void itShouldReturnEmptyFlux() {
        //Given
        PublisherProbe<Customer> probe = PublisherProbe.of(Flux.empty());
        given(underTestRepository.findAll()).willReturn(probe.flux());

        //When
        Flux<Customer> customers = underTestService.getAllCustomers();

        //Then
        StepVerifier.create(customers)
                .verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
        assertEquals(1, probe.subscribeCount());
    }

    @Test
    void itShouldReturnCustomerById() {
        //Given
        PublisherProbe<Customer> probe = PublisherProbe.of(
                Mono.just(customer)
        );
        given(underTestRepository.findById(anyLong())).willReturn(probe.mono());

        //When
        Mono<Customer> result = underTestService.getCustomerById(customer.getId());

        //Then
        StepVerifier.create(result)
                .expectNextMatches(element -> {
                    assertThat(element).satisfies(c -> {
                        assertThat(c.getId()).isEqualTo(1L);
                        assertThat(c.getName()).isEqualTo("testName");
                        assertThat(c.getAge()).isEqualTo(33);
                        assertThat(c.getJob()).isEqualTo("testJob");
                    });
                    return element.equals(customer);
                })
                .verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
        assertEquals(1, probe.subscribeCount());
    }

    @Test
    void itShouldDeleteCustomerByIdAndReturnEmptyMono() {
        //Given
        PublisherProbe<Customer> probe = PublisherProbe.of(
                Mono.just(customer)
        );
        given(underTestRepository.deleteById(customer.getId())).willReturn(probe.mono().empty());

        //When
        Mono<Void> result = underTestService.deleteCustomerById(customer.getId());

        //Then
        StepVerifier.create(result)
                .verifyComplete();
        probe.assertWasNotSubscribed();
        probe.assertWasNotRequested();
        probe.wasCancelled();
    }

    @Test
    void itShouldCreateNewCustomer() {
        //Given
        PublisherProbe<Customer> probe = PublisherProbe.of(Mono.just(customer));
        given(underTestRepository.save(customer)).willReturn(probe.mono());

        //Then
        Mono<Customer> result = underTestService.createCustomer(customer);

        //When
        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void itShouldUpdateCustomer() {
        //Given
        Customer updatedCustomer = new Customer(1L, "updatedName", 22, "updatedJob");
        PublisherProbe<Customer> probe = PublisherProbe.of(Mono.just(updatedCustomer));
        given(underTestRepository.findById(customer.getId())).willReturn(Mono.just(customer));
        given(underTestRepository.save(updatedCustomer)).willReturn(probe.mono());

        //Then
        Mono<Customer> result = underTestService.updateCustomer(customer.getId(), updatedCustomer);

        //When
        StepVerifier.create(result)
                .expectNextMatches(element -> {
                    assertThat(element).satisfies(c -> {
                        assertThat(c.getId()).isEqualTo(1L);
                        assertThat(c.getName()).isEqualTo("updatedName");
                        assertThat(c.getAge()).isEqualTo(22);
                        assertThat(c.getJob()).isEqualTo("updatedJob");
                    });
                    return element.equals(updatedCustomer);
                })
                .verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }
}

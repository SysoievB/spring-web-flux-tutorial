package com.springwebfluxtutorial.service;

import com.springwebfluxtutorial.entities.Customer;
import com.springwebfluxtutorial.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CustomerServicePublisherProbeTest {
    @Mock
    private CustomerRepository underTestRepository;
    @InjectMocks
    private CustomerService underTestService;

    @Test
    void itShouldReturnAllCustomers() {
        //Given
        PublisherProbe<Customer> probe = PublisherProbe.of(
                Flux.just(new Customer(), new Customer(), new Customer())
        );
        given(underTestRepository.findAll()).willReturn(probe.flux());

        //When
        Flux<Customer> customers = underTestService.getAllCustomers();

        //Then
        StepVerifier.create(customers)
                .expectNextCount(3)
                .verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
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
    }
}

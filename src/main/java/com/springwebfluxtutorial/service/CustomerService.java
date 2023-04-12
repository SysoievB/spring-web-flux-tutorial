package com.springwebfluxtutorial.service;

import com.springwebfluxtutorial.entities.Customer;
import com.springwebfluxtutorial.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;

    public Flux<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Mono<Customer> getCustomerById(Long id) {
        return repository.findById(id);
    }

    public Mono<Void> deleteCustomerById(Long id) {
        return repository.deleteById(id);
    }

    public Mono<Customer> createCustomer(Customer customer) {
        return repository.save(customer);
    }

    public Mono<Customer> updateCustomer(Long id, Customer customer) {
        return repository.findById(id)
                .flatMap(existingCustomer -> {
                    existingCustomer.setName(customer.getName());
                    existingCustomer.setAge(customer.getAge());
                    existingCustomer.setJob(customer.getJob());

                    return repository.save(existingCustomer);
                });
    }

    public Mono<Customer> findCustomerByName(final String name) {
        return repository.getCustomerByName(name);
    }
}

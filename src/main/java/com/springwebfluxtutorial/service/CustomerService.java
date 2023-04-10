package com.springwebfluxtutorial.service;

import com.springwebfluxtutorial.entities.Customer;
import com.springwebfluxtutorial.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
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
                .flatMap(oldCustomer -> {
                    oldCustomer.setName(customer.getName());
                    oldCustomer.setAge(customer.getAge());
                    oldCustomer.setJob(customer.getJob());

                    return Mono.just(oldCustomer);
                });
    }
}

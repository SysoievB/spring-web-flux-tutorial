package com.springwebfluxtutorial.controller;

import com.springwebfluxtutorial.entities.Customer;
import com.springwebfluxtutorial.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> getAll() {
        return service.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Customer>> getById(@PathVariable Long id) {
        return service.getCustomerById(id)
                .map(customer -> ResponseEntity.ok(customer))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public Mono<ResponseEntity<Customer>> getByName(@PathVariable String name) {
        return service.findCustomerByName(name)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> delete(@PathVariable Long id) {
        return service.deleteCustomerById(id);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Customer>> update(@PathVariable Long id, @RequestBody Customer customer) {
        return service.updateCustomer(id, customer)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> save(@RequestBody Customer customer) {
        return service.createCustomer(customer);
    }
}

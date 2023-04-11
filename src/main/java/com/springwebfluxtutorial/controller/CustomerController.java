package com.springwebfluxtutorial.controller;

import com.springwebfluxtutorial.entities.Customer;
import com.springwebfluxtutorial.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/customer")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    Flux<Customer> getAll() {
        return service.getAllCustomers();
    }
}

package com.springwebfluxtutorial;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class SpringWebFluxTutorialApplicationTests {

    @Test
    void fluxTest() {
        //Given
        Flux<String> stringFlux = Flux.just("Spring Core", "Spring Boot", "Spring Web")
                .concatWith(Flux.just("After Error"))
                .log();

        //When
        stringFlux
                .subscribe(System.out::println,
                        e -> System.out.println("Exception is " + e),
                        () -> System.out.println("Completed")
                );

        //Then
        StepVerifier.create(stringFlux)
                .expectNext("Spring Core", "Spring Boot", "Spring Web", "After Error")
                .verifyComplete();
    }

    @Test
    void fluxOnErrorTest() {
        //Given
        Flux<String> stringFlux = Flux.just("Spring Core", "Spring Boot", "Spring Web")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .log();

        //Then
        StepVerifier.create(stringFlux)
                .expectNext("Spring Core")
                .expectNext("Spring Boot")
                .expectNext("Spring Web")
                //.expectError(RuntimeException.class)
                .expectErrorMessage("Error Occurred")
                .verify();
    }
}

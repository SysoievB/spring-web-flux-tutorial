package com.springwebfluxtutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableR2dbcRepositories
@EnableWebFlux
public class SpringWebFluxTutorialApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebFluxTutorialApplication.class, args);
    }
}

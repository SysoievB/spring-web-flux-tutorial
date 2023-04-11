package com.springwebfluxtutorial.config;

import io.r2dbc.spi.ConnectionFactory;
import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {

    @Autowired
    private R2dbcProperties r2dbcProperties;

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        MySqlConnectionConfiguration configuration = MySqlConnectionConfiguration.builder()
                .host(r2dbcProperties.getHost())
                .port(r2dbcProperties.getPort())
                .database(r2dbcProperties.getDatabase())
                .username(r2dbcProperties.getUsername())
                .password(r2dbcProperties.getPassword())
                .build();

        return MySqlConnectionFactory.from(configuration);
    }
}


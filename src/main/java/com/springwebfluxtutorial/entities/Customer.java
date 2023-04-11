package com.springwebfluxtutorial.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class Customer {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private Integer age;

    @Column
    private String job;
}

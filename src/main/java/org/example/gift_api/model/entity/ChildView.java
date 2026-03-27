package org.example.gift_api.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class ChildView {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer age;
    private Integer presentsCount;
}

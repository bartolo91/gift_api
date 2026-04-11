package org.example.gift_api.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

@Entity
@Immutable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChildView {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer age;
    private Integer presentsCount;
}

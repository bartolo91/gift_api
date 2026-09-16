package org.example.gift_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChildDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Long presentsCount;
    private Long version;
    private String email;
}

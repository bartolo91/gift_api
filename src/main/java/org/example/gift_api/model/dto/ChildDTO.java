package org.example.gift_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChildDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Long presentsCount;
    private Long version;
    private String email;
}

package org.example.gift_api.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDate;

@Getter
@Builder
public class ChildDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private int presentAmount;
}

package org.example.gift_api.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ChildDTO {

    public ChildDTO(Long id, String firstName, String lastName, LocalDate birthDate, Long presentCount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.presentCount = presentCount;
    }

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Long presentCount;
}

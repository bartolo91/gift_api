package org.example.gift_api.model.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateChildCommand {

    @NotBlank(message = "FIRST_NAME_NOT_EMPTY_OR_NULL")
    private String firstName;

    @NotBlank(message = "LAST_NAME_NOT_EMPTY_OR_NULL")
    private String lastName;

    @NotNull(message = "BIRTH_DATE_NOT_NULL")
    @Past(message = "BIRTH_DATE_NOT_FUTURE")
    private LocalDate birthDate;
}

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

    @NotBlank(message = "NO_VALUE")
    private String firstName;

    @NotBlank(message = "NO_VALUE")
    private String lastName;

    @NotNull(message = "NULL_VALUE")
    @Past(message = "FUTURE_VALUE")
    private LocalDate birthDate;
}

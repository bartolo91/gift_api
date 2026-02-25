package org.example.gift_api.model.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.AttributeAccessor;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateChildCommand {

    //wersja

    @NotEmpty(message = "FIRST_NAME_NOT_EMPTY")
    private String firstName;

    @NotEmpty(message = "LAST_NAME_NOT_EMPTY")
    private String lastName;

    @NotEmpty(message = "BIRTH_DATE_NOT_EMPTY")
    @Past
    private LocalDate birthDate;
}

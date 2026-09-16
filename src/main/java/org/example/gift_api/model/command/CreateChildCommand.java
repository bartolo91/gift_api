package org.example.gift_api.model.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import org.example.gift_api.validation.UniqueEmail;
import org.example.gift_api.validation.UniqueFirstAndLastName;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
//@UniqueFirstAndLastName
public class CreateChildCommand {

    private String type;
    private Map<String, String> params;

//    @NotBlank(message = "NO_VALUE")
//    private String firstName;
//
//    @NotBlank(message = "NO_VALUE")
//    private String lastName;
//
//    @NotNull(message = "NULL_VALUE")
//    @Past(message = "FUTURE_VALUE")
//    private LocalDate birthDate;
//
//    @UniqueEmail
//    @NotBlank(message = "NO_VALUE")
//    private String email;
}

package org.example.gift_api.model.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePresentCommand {

    @NotBlank(message = "NO_VALUE")
    private String name;

    @NotNull(message = "NULL_VALUE")
    @Positive(message = "POSITIVE_VALUE")
    private BigDecimal price;
}

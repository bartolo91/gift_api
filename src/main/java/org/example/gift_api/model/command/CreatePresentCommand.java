package org.example.gift_api.model.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePresentCommand {

    @NotBlank(message = "NAME_NOT_EMPTY_OR_NULL")
    private String name;

    @NotNull
    @Positive
    private BigDecimal price;
}

package org.example.gift_api.model.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PresentCommand {

    @NotEmpty
    private String name;

    @NotEmpty
    @Positive
    private BigDecimal price;
}

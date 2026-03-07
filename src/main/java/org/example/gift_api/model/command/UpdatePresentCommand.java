package org.example.gift_api.model.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class UpdatePresentCommand {

    //wersja

    @NotBlank
    private String name;

    @NotBlank
    private BigDecimal price;
}

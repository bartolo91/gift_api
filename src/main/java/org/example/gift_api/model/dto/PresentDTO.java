package org.example.gift_api.model.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class PresentDTO {

    private Long id;
    private String name;
    private BigDecimal price;
}

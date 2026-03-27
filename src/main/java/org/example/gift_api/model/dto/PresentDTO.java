package org.example.gift_api.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PresentDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long version;
}

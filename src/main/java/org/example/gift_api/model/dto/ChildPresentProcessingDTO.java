package org.example.gift_api.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChildPresentProcessingDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Long presentsCount;
}

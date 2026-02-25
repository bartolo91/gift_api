package org.example.gift_api.exceptions.types;

import lombok.Value;

@Value
public class EntityNotFoundException extends RuntimeException{

    private Class<?> type;
    private Long id;
}

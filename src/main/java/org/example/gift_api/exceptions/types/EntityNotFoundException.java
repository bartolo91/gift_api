package org.example.gift_api.exceptions.types;


public class EntityNotFoundException extends RuntimeException {

    private Class<?> type;
    private Long id;

    public EntityNotFoundException(Class<?> type, Long id) {
        super(type.getSimpleName() + " with id: " + id + " not found");
    }
}

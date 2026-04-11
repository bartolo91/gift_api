package org.example.gift_api.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorDTO extends ExceptionDTO {

    private static final String MESSAGE = "Validation errors";
    private final List<ViolationInfo> violations = new ArrayList<>();

    public ValidationErrorDTO() {
        super(MESSAGE);
    }

    public void addViolation(String field, String message) {
        violations.add(new ViolationInfo(field, message));
    }

    private record ViolationInfo(String field, String message) {}
}

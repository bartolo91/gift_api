package org.example.gift_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.gift_api.repository.ChildRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final ChildRepository childRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String emial, ConstraintValidatorContext context) {
        return !childRepository.existsByEmail(emial);
    }
}

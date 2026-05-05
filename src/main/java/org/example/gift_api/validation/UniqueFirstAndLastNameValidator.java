package org.example.gift_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.command.UpdateChildCommand;
import org.example.gift_api.repository.ChildRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniqueFirstAndLastNameValidator implements ConstraintValidator<UniqueFirstAndLastName, CreateChildCommand> {

    private final ChildRepository repository;

    @Override
    public void initialize(UniqueFirstAndLastName constraintAnnotation) {
    }

    @Override
    public boolean isValid(CreateChildCommand command, ConstraintValidatorContext context) {
        if (command.getFirstName() == null || command.getLastName() == null) {
            return true;
        }

        return !repository.existsByFirstNameAndLastName(command.getFirstName(), command.getLastName());
    }
}
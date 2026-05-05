package org.example.gift_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = UniqueFirstAndLastNameValidator.class)
public @interface UniqueFirstAndLastName {

    String message() default "FIRST_NAME_AND_LAST_NAME_NOT_UNIQUE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
package org.example.gift_api.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GiftApiException.class)
    public ResponseEntity<ExceptionDTO> handleGiftApiException(GiftApiException e) {
        return new ResponseEntity<>(new ExceptionDTO(e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ValidationErrorDTO errorDTO = new ValidationErrorDTO();
        ex.getFieldErrors().forEach(error ->
                errorDTO.addViolation(error.getField(), error.getDefaultMessage()));
        ex.getGlobalErrors().forEach(error ->
                errorDTO.addViolation(error.getObjectName(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errorDTO);
    }
}

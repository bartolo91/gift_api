package org.example.gift_api.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@RequiredArgsConstructor
public class ExceptionDTO {

    private final LocalDateTime timestamp = now();
    private final String message;
}

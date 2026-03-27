package org.example.gift_api.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public class GiftApiException extends RuntimeException {

    private HttpStatus status;

    public GiftApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static GiftApiException notFound(Class<?> type, Long id) {
        String message = format("{0} with id {1} not found", type, id);
        return new GiftApiException(message, NOT_FOUND);
    }
}

package org.example.gift_api.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public class GiftApiException extends RuntimeException {

    private HttpStatus status;

    public GiftApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static GiftApiException notFound(Class<?> type, Long id) {
        String message = "ENTITY_NOT_FOUND ID: " + id;
        return new GiftApiException(message, NOT_FOUND);
    }

    public static GiftApiException badRequest() {
        return new GiftApiException("Resource was modified by another request", BAD_REQUEST);
    }

    public static GiftApiException exceededCountOfPresents() {
        return new GiftApiException("Child cannot have more than 3 presents", BAD_REQUEST);
    }

    public static GiftApiException concurrentModification() {
        return new GiftApiException("The resource was modified concurrently. Please retry the request.", CONFLICT);
    }
}

package io.github.luviuche.hotel.exception;

import java.time.LocalDateTime;

/**
 * Uniform JSON body for every error returned by the API.
 */
public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, message, LocalDateTime.now());
    }
}

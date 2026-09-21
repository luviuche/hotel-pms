package io.github.luviuche.hotel.exception;

/**
 * Thrown when an operation breaks a business or validation rule,
 * such as a duplicate email or inconsistent dates.
 * The global handler turns it into an HTTP 400 response.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}

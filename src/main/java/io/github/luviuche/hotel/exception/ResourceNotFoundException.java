package io.github.luviuche.hotel.exception;

/**
 * Thrown when a resource is requested by an id that does not exist.
 * The global handler turns it into an HTTP 404 response.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " with id " + id + " was not found.");
    }
}

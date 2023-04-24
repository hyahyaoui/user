package com.knsoft.user.exceptions;

/**
 * Thrown to indicate that a user registration request has expired.
 *
 * <p>This exception is typically thrown when a user attempts to validate a registration request
 * that has been inactive for longer than the allowed time lapse.</p>
 *
 * @author hyahyaoui
 * @version 1.0
 * @since 1.0.0
 */
public class RegistrationRequestExpiredException extends RuntimeException {

    public RegistrationRequestExpiredException(String message) {
        super(message);
    }
}

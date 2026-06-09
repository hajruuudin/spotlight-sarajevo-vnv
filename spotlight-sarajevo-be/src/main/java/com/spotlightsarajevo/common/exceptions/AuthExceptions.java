package com.spotlightsarajevo.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.spotlightsarajevo.modules.auth.service.AuthServiceImpl;

public class AuthExceptions {
    /**
     * Thrown when an error occurs during Google-based authentication or registration.
     *
     * <p>This exception is typically thrown from {@link AuthServiceImpl}
     * when Google OAuth validation or user data extraction fails.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class GoogleRegisterException extends RuntimeException {
        public GoogleRegisterException(String message){
            super(message);
        }
    }

    /**
     * Thrown when an error occurs during System-based authentication or registration.
     *
     * <p>This exception is typically thrown from {@link AuthServiceImpl}
     * when system validation or user data extraction fails.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class SystemRegisterException extends RuntimeException {
        public SystemRegisterException(String message){
            super(message);
        }
    }

    /**
     * Thrown when an error occurs during data saving within the database.
     *
     * <p>This exception is typically thrown from {@link AuthServiceImpl}
     * when registering data (System or Google) into the database tables.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class UserRegisterException extends RuntimeException {
        public UserRegisterException(String message){
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static class GoogleLoginException extends RuntimeException {
        public GoogleLoginException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static class SystemLoginException extends RuntimeException {
        public SystemLoginException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class AuthenticationEndpointException extends RuntimeException {
        public AuthenticationEndpointException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class AuthenticationUserNotFoundException extends RuntimeException {
        public AuthenticationUserNotFoundException(String message){ super(message);}
    }
}

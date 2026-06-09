package com.spotlightsarajevo.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    /*==== SPOT EXCEPTIONS START ====*/
    @ExceptionHandler(SpotExceptions.SpotNotFoundException.class)
    public ResponseEntity<?> handleSpotNotFoundException(SpotExceptions.SpotNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Spot Find Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(SpotExceptions.SpotSystemException.class)
    public ResponseEntity<?> handleSpotSystemException(SpotExceptions.SpotSystemException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Spot System Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(SpotExceptions.SpotCreationException.class)
    public ResponseEntity<?> handleSpotCreationException(SpotExceptions.SpotCreationException ex){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Spot Creation Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(SpotExceptions.SpotConflictException.class)
    public ResponseEntity<?> handleSpotConflictException(SpotExceptions.SpotConflictException ex){
        return buildResponse(HttpStatus.CONFLICT, "Spot Conflict Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(SpotExceptions.SpotUnauthorizedException.class)
    public ResponseEntity<?> handleSpotUnauthorisedException(SpotExceptions.SpotUnauthorizedException ex){
        return buildResponse(HttpStatus.UNAUTHORIZED, "Spot Unauthorised Error", ex.getMessage(), ex.toString());
    }
    /*==== SPOT EXCEPTIONS END ====*/

    /*==== EVENT EXCEPTIONS START ====*/
    @ExceptionHandler(EventExceptions.EventNotFoundException.class)
    public ResponseEntity<?> handleEventNotFoundException(EventExceptions.EventNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Event Find Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(EventExceptions.EventSystemException.class)
    public ResponseEntity<?> handleEventSystemException(EventExceptions.EventSystemException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Event System Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(EventExceptions.EventCreationException.class)
    public ResponseEntity<?> handleEventCreationException(EventExceptions.EventCreationException ex){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Event Creation Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(EventExceptions.EventConflictException.class)
    public ResponseEntity<?> handleEventConflictException(EventExceptions.EventConflictException ex){
        return buildResponse(HttpStatus.CONFLICT, "Event Conflict Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(EventExceptions.EventUnauthorizedException.class)
    public ResponseEntity<?> handleEventUnauthorisedException(EventExceptions.EventUnauthorizedException ex){
        return buildResponse(HttpStatus.UNAUTHORIZED, "Event Unauthorised Error", ex.getMessage(), ex.toString());
    }
    /*==== EVENT EXCEPTIONS END ====*/

    /*==== AUTHENTICATION EXCEPTIONS START ====*/
    @ExceptionHandler(AuthExceptions.UserRegisterException.class)
    public ResponseEntity<?> handleUserDataRegisterException(AuthExceptions.UserRegisterException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "User Data Registration Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(AuthExceptions.GoogleLoginException.class)
    public ResponseEntity<?> handleGoogleLoginException(AuthExceptions.GoogleLoginException ex){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Google Login Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(AuthExceptions.SystemLoginException.class)
    public ResponseEntity<?> handleSystemLoginException(AuthExceptions.SystemLoginException ex){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "System Login Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(AuthExceptions.AuthenticationEndpointException.class)
    public ResponseEntity<?> handleAuthEndpointException(AuthExceptions.AuthenticationEndpointException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Authentication Endpoint Error", ex.getMessage(), ex.toString());
    }
    /*==== AUTHENTICATION EXCEPTIONS END ====*/

    /*==== TRANSPORT EXCEPTIONS START ====*/
    @ExceptionHandler(TransportExceptions.TransportNotFoundException.class)
    public ResponseEntity<?> handleTransportNotFoundException(TransportExceptions.TransportNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Transport Find Error", ex.getMessage(), ex.toString());
    }

    @ExceptionHandler(TransportExceptions.TransportBadRequestException.class)
    public ResponseEntity<?> handleTransportBadRequestException(TransportExceptions.TransportBadRequestException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Transport Request Error", ex.getMessage(), ex.toString());
    }
    /*==== TRANSPORT EXCEPTIONS END ====*/

    /* General Exception handler for anything not specified */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", ex.getMessage(), ex.toString());
    }

    /* Build method used to create custom messages */
    private ResponseEntity<?> buildResponse(HttpStatus status, String error, String message, String toString) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "error", error,
                "messageBa", message,
                "stackTrace", toString
        ));
    }
}

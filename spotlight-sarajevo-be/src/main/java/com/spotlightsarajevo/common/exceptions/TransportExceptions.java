package com.spotlightsarajevo.common.exceptions;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.modules.transport.service.TransportServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class TransportExceptions {
    /**
     * Thrown when a transport method is not found in the database.
     *
     * <p>This exception is typically thrown from {@link TransportServiceImpl}
     * when calling any method that attempts to find transport methods based on certain criteria.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class TransportNotFoundException extends RuntimeException {
        public TransportNotFoundException(ExceptionCodes.TransportExceptionCodes message) {
            super(message.toString());
        }
    }

    /**
     * Thrown when an invalid request is made to the transport service.
     *
     * <p>This exception is typically thrown from {@link TransportServiceImpl}
     * when providing parameters which do not conform to the required method specification.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class TransportBadRequestException extends RuntimeException {
        public TransportBadRequestException(ExceptionCodes.TransportExceptionCodes message) {
            super(message.toString());
        }
    }
}

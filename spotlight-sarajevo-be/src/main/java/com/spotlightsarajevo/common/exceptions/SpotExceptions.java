package com.spotlightsarajevo.common.exceptions;

import com.spotlightsarajevo.modules.spot.service.SpotServiceImpl;
import com.spotlightsarajevo.common.enums.ExceptionCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SpotExceptions {
    /**
     * Thrown when an error occurs during any method that attempts to retrieve Spot information.
     *
     * <p>This exception is typically thrown from {@link SpotServiceImpl}
     * when calling any method that attempts to find spots based on certain criteria.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class SpotNotFoundException extends RuntimeException {
        public SpotNotFoundException(ExceptionCodes.SpotExceptionCodes message){
            super(message.toString());
        }
    }

    /**
     * Thrown when an error occurs during any method that attempts persist new Spot Objects.
     *
     * <p>This exception is typically thrown from {@link SpotServiceImpl}
     * when an admin attempts to persist new spots into the database.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class SpotCreationException extends RuntimeException {
        public SpotCreationException(ExceptionCodes.SpotExceptionCodes message) {
            super(message.toString());
        }
    }

    /**
     * Thrown when an error occurs during any unauthorised exception in Spot Service methods.
     *
     * <p>This exception is typically thrown from {@link SpotServiceImpl}
     * when an unauthorised user attempts to modify, add or find data which he does not have permission for</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class SpotUnauthorizedException extends RuntimeException {
        public SpotUnauthorizedException(ExceptionCodes.SpotExceptionCodes message) {
            super(message.toString());
        }
    }

    /**
     * Thrown when an error occurs during spot creation and updating conflicts.
     *
     * <p>This exception is typically thrown from {@link SpotServiceImpl}
     * when an admin attempts to persist new spots into the database without following unique value constraints.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    public static class SpotConflictException extends RuntimeException {
        public SpotConflictException(ExceptionCodes.SpotExceptionCodes message) {
            super(message.toString());
        }
    }

    /**
     * Thrown when an error occurs during any spot operation which could contain invalid input formats.
     *
     * <p>This exception is typically thrown from {@link SpotServiceImpl}
     * when providing parameters which do not conform to the required method specification (e.g. providing null params).</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class SpotSystemException extends RuntimeException {
        public SpotSystemException(ExceptionCodes.SpotExceptionCodes message) {
            super(message.toString());
        }
    }

    /**
     * Thrown when an error occurs during any spot review operation which could contain invalid input formats.
     *
     * <p>This exception is typically thrown from {@link SpotServiceImpl}
     * when providing parameters which do not conform to the required method specification (e.g. providing null params).</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class SpotReviewException extends RuntimeException {
        public SpotReviewException(ExceptionCodes.SpotExceptionCodes message) {
            super(message.toString());
        }
    }
}

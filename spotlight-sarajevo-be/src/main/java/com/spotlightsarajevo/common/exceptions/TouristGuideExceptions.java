package com.spotlightsarajevo.common.exceptions;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class TouristGuideExceptions {
    /**
     * Thrown when there is an error with a tourist guide request.
     *
     * <p>This exception is typically thrown from tourist guide-related services
     * when a request to create or modify a tourist guide fails due to invalid data or other issues.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class TouristGuideRequestException extends RuntimeException{
        public TouristGuideRequestException(ExceptionCodes.TouristGuideExceptionCodes message){
            super(message.toString());
        }
    }

    /**
     * Thrown when a specified tourist guide cannot be found in the database.
     *
     * <p>This exception is typically thrown from tourist guide-related services
     * when attempting to retrieve or manipulate a tourist guide that does not exist.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class TouristGuideNotFound extends RuntimeException{
        public TouristGuideNotFound(ExceptionCodes.TouristGuideExceptionCodes message){
            super(message.toString());
        }
    }

    /**
     * Thrown when a user is not authorized to access or modify a tourist guide.
     *
     * <p>This exception is typically thrown from tourist guide-related services
     * when a user attempts to perform an action without the necessary permissions.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class TouristGuideAuth extends RuntimeException{
        public TouristGuideAuth(ExceptionCodes.TouristGuideExceptionCodes message){
            super(message.toString());
        }
    }

    /**
     * Thrown when there is a conflict during tourist guide creation or updating.
     *
     * <p>This exception is typically thrown from tourist guide-related services
     * when attempting to create or update a tourist guide that violates unique constraints or other business rules.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    public static class TouristGuideConflict extends RuntimeException{
        public TouristGuideConflict(ExceptionCodes.TouristGuideExceptionCodes message){
            super(message.toString());
        }
    }
}

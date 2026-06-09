package com.spotlightsarajevo.common.exceptions;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MediaExceptions {
    /**
     * Thrown when there is an error creating media.
     *
     * <p>This exception is typically thrown from media-related services
     * when a request to create media fails due to invalid data or other issues.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class MediaCreateException extends RuntimeException{
        public MediaCreateException(ExceptionCodes.MediaExceptionCodes message){
            super(message.toString());
        }
    }
}

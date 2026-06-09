package com.spotlightsarajevo.common.exceptions;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CollectionExceptions {
    /**
     * Thrown when a user is not authorized to access or modify a collection.
     *
     * <p>This exception is typically thrown from collection-related services
     * when a user attempts to perform an action without the necessary permissions.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class CollectionUnauthorisedException extends RuntimeException{
        public CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes message){
            super(message.toString());
        }
    }

    /**
     * Thrown when a request to create or modify a collection contains invalid or malformed data.
     *
     * <p>This exception is typically thrown from collection-related services
     * when the provided data does not meet the required criteria.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class CollectionRequestContentException extends RuntimeException{
        public CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes message){
            super(message.toString());
        }
    }

    /**
     * Thrown when a specified collection cannot be found in the database.
     *
     * <p>This exception is typically thrown from collection-related services
     * when attempting to retrieve or manipulate a collection that does not exist.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class CollectionNotFoundException extends RuntimeException{
        public CollectionNotFoundException(ExceptionCodes.CollectionExceptionCodes message){
            super(message.toString());
        }
    }

    /**
     * Thrown when a specified item within a collection cannot be found.
     *
     * <p>This exception is typically thrown from collection-related services
     * when attempting to access or modify an item that does not exist within the specified collection.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class CollectionItemNotFoundException extends RuntimeException{
        public CollectionItemNotFoundException(ExceptionCodes.CollectionExceptionCodes message){
            super(message.toString());
        }
    }

    /**
     * Thrown when a system-level error occurs within collection operations.
     *
     * <p>This exception is typically thrown from collection-related services
     * when an unexpected error occurs that prevents the completion of the requested operation.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static class CollectionSystemException extends RuntimeException{
        public CollectionSystemException(ExceptionCodes.CollectionExceptionCodes message){
            super(message.toString());
        }
    }

    /**
     * Thrown when there is a naming conflict during collection creation or modification.
     *
     * <p>This exception is typically thrown from collection-related services
     * when attempting to create or rename a collection to a name that already exists.</p>
     *
     * @see GlobalExceptionHandler
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    public static class CollectionNamingException extends  RuntimeException{
        public CollectionNamingException(ExceptionCodes.CollectionExceptionCodes message) {
            super(message.toString());
        }
    }
}

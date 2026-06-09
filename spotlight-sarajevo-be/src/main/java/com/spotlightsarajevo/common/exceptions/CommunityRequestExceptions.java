package com.spotlightsarajevo.common.exceptions;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CommunityRequestExceptions {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class UnauthorizedAccess extends RuntimeException{
        public UnauthorizedAccess(ExceptionCodes.CommunityRequestExceptionCodes message) {
            super(message.toString());
        };
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class RequestNotFound extends RuntimeException{
        public RequestNotFound(ExceptionCodes.CommunityRequestExceptionCodes message){
            super(message.toString());
        }
    }
}

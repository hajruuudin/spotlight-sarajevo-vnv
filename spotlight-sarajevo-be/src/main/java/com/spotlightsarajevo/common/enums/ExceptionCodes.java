package com.spotlightsarajevo.common.enums;

public class ExceptionCodes {
    public enum SpotExceptionCodes {
        INVALID_REQUEST_CONTENT,
        SPOT_NOT_FOUND,
        EMPTY_SPOT_REQUEST_BODY,
        SPOT_UNAUTHORIZED_ACCESS,
        SPOT_UPDATE_EXCEPTION,
        SPOT_REVIEW_NOT_FOUND,
        SPOT_REVIEW_CONFLICT
    }

    public enum EventExceptionCodes {
        INVALID_REQUEST_CONTENT,
        EVENT_NOT_FOUND,
        EVENT_UNAUTHORIZED_ACCESS,
        EVENT_UPDATE_EXCEPTION,
        EVENT_ORGANISER_REVIEW_NOT_FOUND
    }

    public enum CollectionExceptionCodes {
        COLLECTION_FIND_UNAUTHORISED,
        COLLECTION_REQUEST_INVALID,
        COLLECTION_NOT_FOUND,
        COLLECTION_UNAUTHORISED_ACCESS,
        COLLECTION_INVALID_TYPE,
        COLLECTION_ITEM_NOT_FOUND,
        COLLECTION_SYSTEM_EXCEPTION,
        COLLECTION_NAME_CONFLICT
    }

    public enum TouristGuideExceptionCodes {
        TOURIST_GUIDE_BAD_REQUEST,
        TOURIST_GUIDE_NOT_FOUND,
        TOURIST_GUIDE_C_BR,
        TOURIST_GUIDE_C_AU,
        TOURIST_GUIDE_C_SLUG,
    }

    public enum MediaExceptionCodes {
        MEDIA_C_BR
    }

    public enum TransportExceptionCodes {
        TRANSPORT_METHOD_NOT_FOUND,
        TRANSPORT_INVALID_REQUEST
    }

    public enum AuthExceptionCodes {
        AUTH_INVALID_CREDENTIALS,
        AUTH_USER_NOT_FOUND,
        AUTH_UNAUTHORIZED_ACCESS,
        AUTH_TOKEN_EXPIRED,
        AUTH_TOKEN_INVALID,
        AUTH_SPOT_ID_MISSING,
        AUTH_EVENT_ID_MISSING
    }

    public enum CommunityRequestExceptionCodes {
        CR_UNAUTHORIZED,
        CR_INVALID_REQUEST_BODY,
        CR_REQUEST_NOT_FOUND
    }
}

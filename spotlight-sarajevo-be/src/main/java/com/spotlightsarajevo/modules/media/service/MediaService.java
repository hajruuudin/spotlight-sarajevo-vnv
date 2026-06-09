package com.spotlightsarajevo.modules.media.service;

import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import org.springframework.http.ResponseEntity;

/**
 * MediaService interface defines methods for media-related operations.
 * It includes methods for looking up thumbnail images, retrieving all images,
 */
public interface MediaService {
    /**
     * Looks up the thumbnail image for a given object model.
     * @param objectModel the object model to look up the thumbnail for
     * @param objectType the type of the object
     * @param objectId the unique identifier of the object
     */
    <T> void lookupThumbnailImage(T objectModel, ObjectType objectType, Integer objectId);

    /**
     * Looks up all images for a given object model.
     * @param objectModel the object model to look up images for
     * @param objectType the type of the object
     * @param objectId the unique identifier of the object
     */
    <T> void lookupAllImages(T objectModel, ObjectType objectType, Integer objectId);

    /**
     * Creates a new media store entry based on the provided MediaStoreCreateModel.
     * @param request the model containing information to create a new media store entry
     * @return ResponseEntity containing the created MediaStoreModel
     */
    ResponseEntity<MediaStoreModel> create(MediaStoreCreateModel request);
}

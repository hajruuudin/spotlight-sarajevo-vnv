package com.spotlightsarajevo.modules.media.utils;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.exceptions.MediaExceptions;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import com.spotlightsarajevo.modules.media.domain.MediaStoreDAO;
import com.spotlightsarajevo.modules.media.domain.entity.MediaStoreEntity;
import com.spotlightsarajevo.modules.media.mapper.MediaStoreMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MediaUtilities is a comprehensive utility component for handling all media-related operations.
 * It consolidates functionality for finding, creating, updating, and deleting media files.
 */
@Component
@AllArgsConstructor
public class MediaUtilities {

    private final MediaStoreDAO mediaStoreDAO;
    private final MediaStoreMapper mediaStoreMapper;

    /*=============================================================================*/
    /*============================== LOOKUP METHODS ==============================*/
    /*=============================================================================*/

    /**
     * Looks up the thumbnail image for a given object model and populates it.
     * If no thumbnail exists, sets a fallback image.
     *
     * @param objectModel the object model to populate with thumbnail
     * @param objectType the type of the object
     * @param objectId the unique identifier of the object
     */
    public <T> void lookupThumbnailImage(T objectModel, ObjectType objectType, Integer objectId) {
        if (objectModel == null || objectType == null || objectId == null) {
            return;
        }

        MediaStoreEntity thumbnailEntity = mediaStoreDAO.findThumbnailByItemIdAndItemCategory(objectId, objectType);

        if (thumbnailEntity != null) {
            MediaStoreModel thumbnailModel = mediaStoreMapper.entityToDto(thumbnailEntity);
            setThumbnailOnObject(objectModel, thumbnailModel);
        } else {
            // Set fallback image as MediaStoreModel with no delete URL
            MediaStoreModel fallbackModel = new MediaStoreModel();
            fallbackModel.setImageUrl("https://i.ibb.co/W472hxLk/FALLBACK.png");
            fallbackModel.setImageDeleteUrl(null);
            fallbackModel.setIsThumbnail(true);
            setThumbnailOnObject(objectModel, fallbackModel);
        }
    }

    /**
     * Looks up all non-thumbnail images for a given object model and populates them.
     *
     * @param objectModel the object model to populate with images
     * @param objectType the type of the object
     * @param objectId the unique identifier of the object
     */
    public <T> void lookupAllImages(T objectModel, ObjectType objectType, Integer objectId) {
        if (objectModel == null || objectType == null || objectId == null) {
            return;
        }

        List<MediaStoreEntity> imageEntities = mediaStoreDAO.findAllNonThumbnailByItemIdAndItemCategory(objectId, objectType);

        if (imageEntities != null && !imageEntities.isEmpty()) {
            List<MediaStoreModel> imageModels = mediaStoreMapper.entitiesToModels(imageEntities);
            try {
                Method setImagesMethod = objectModel.getClass().getMethod("setImages", List.class);
                setImagesMethod.invoke(objectModel, imageModels);
            } catch (NoSuchMethodException e) {
                System.err.println("Object of type " + objectModel.getClass().getName() + " does not have a setImages(List) method.");
            } catch (Exception e) {
                System.err.println("Error invoking setImages method on " + objectModel.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Helper method to set thumbnail image on an object using reflection.
     * Tries to set as MediaStoreModel first, then falls back to String URL setter.
     *
     * @param objectModel the object to set thumbnail on
     * @param thumbnailModel the thumbnail model to set
     */
    private <T> void setThumbnailOnObject(T objectModel, MediaStoreModel thumbnailModel) {
        try {
            // Try to set as MediaStoreModel first (for overview models)
            Method setThumbnailMethod = objectModel.getClass().getMethod("setThumbnailImage", MediaStoreModel.class);
            setThumbnailMethod.invoke(objectModel, thumbnailModel);
        } catch (NoSuchMethodException e) {
            // Fall back to String setter (for shorthand models that only need URL)
            try {
                Method setImageURLMethod = objectModel.getClass().getMethod("setThumbnailImage", String.class);
                setImageURLMethod.invoke(objectModel, thumbnailModel.getImageUrl());
            } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException ex) {
                System.err.println("Object of type " + objectModel.getClass().getName() + " does not have a setThumbnailImage method.");
            }
        } catch (Exception e) {
            System.err.println("Error invoking setThumbnailImage method on " + objectModel.getClass().getName() + ": " + e.getMessage());
        }
    }

    /*=============================================================================*/
    /*============================== CREATE METHODS ==============================*/
    /*=============================================================================*/

    /**
     * Creates a new media store entry based on the provided MediaStoreCreateModel.
     *
     * @param request the model containing information to create a new media store entry
     * @return the created MediaStoreModel
     */
    public MediaStoreModel create(MediaStoreCreateModel request) {
        if (request == null) {
            throw new MediaExceptions.MediaCreateException(ExceptionCodes.MediaExceptionCodes.MEDIA_C_BR);
        }

        MediaStoreEntity entity = mediaStoreMapper.dtoToEntity(request);
        MediaStoreEntity addedEntity = mediaStoreDAO.save(entity);

        return mediaStoreMapper.entityToDto(addedEntity);
    }

    /**
     * Adds a single thumbnail image for a given item.
     * If a thumbnail already exists, it will be replaced.
     *
     * @param thumbnailModel the new thumbnail image model
     * @param itemId the ID of the item
     * @param objectType the type of the object (e.g., SPOT, EVENT, ORGANISER)
     * @param username the username of the user performing the operation
     */
    public void addThumbnailImage(MediaStoreCreateModel thumbnailModel, Integer itemId, ObjectType objectType, String username) {
        if (thumbnailModel == null) return;

        // Delete existing thumbnail if present
        mediaStoreDAO.deleteByItemIdAndItemCategoryAndIsThumbnailTrue(itemId, objectType);

        // Create and save new thumbnail
        thumbnailModel.setItemId(itemId);
        thumbnailModel.setItemCategory(objectType.toString());
        thumbnailModel.setIsThumbnail(true);
        thumbnailModel.setCreated(LocalDateTime.now());
        thumbnailModel.setCreatedBy(username);
        create(thumbnailModel);
    }

    /**
     * Adds new regular (non-thumbnail) images for a given item.
     *
     * @param imagesToAdd the list of images to add
     * @param itemId the ID of the item
     * @param objectType the type of the object (e.g., SPOT, EVENT)
     * @param username the username of the user performing the operation
     */
    public void addImages(List<MediaStoreCreateModel> imagesToAdd, Integer itemId, ObjectType objectType, String username) {
        if (imagesToAdd == null || imagesToAdd.isEmpty()) return;

        for (MediaStoreCreateModel imageModel : imagesToAdd) {
            imageModel.setItemId(itemId);
            imageModel.setItemCategory(objectType.toString());
            imageModel.setIsThumbnail(false);
            imageModel.setCreated(LocalDateTime.now());
            imageModel.setCreatedBy(username);
            create(imageModel);
        }
    }

    /*=============================================================================*/
    /*============================== UPDATE METHODS ==============================*/
    /*=============================================================================*/

    /**
     * Updates the thumbnail image for a given item. Deletes the existing thumbnail and creates a new one.
     *
     * @param thumbnailModel the new thumbnail image model
     * @param itemId the ID of the item
     * @param objectType the type of the object (e.g., SPOT, EVENT, ORGANISER)
     * @param username the username of the user performing the update
     */
    public void updateThumbnailImage(MediaStoreCreateModel thumbnailModel, Integer itemId, ObjectType objectType, String username) {
        if (thumbnailModel == null) return;

        mediaStoreDAO.deleteByItemIdAndItemCategoryAndIsThumbnailTrue(itemId, objectType);

        thumbnailModel.setItemId(itemId);
        thumbnailModel.setItemCategory(objectType.toString());
        thumbnailModel.setIsThumbnail(true);
        thumbnailModel.setCreated(LocalDateTime.now());
        thumbnailModel.setCreatedBy(username);
        create(thumbnailModel);
    }

    /*=============================================================================*/
    /*============================== DELETE METHODS ==============================*/
    /*=============================================================================*/

    /**
     * Deletes images by their IDs.
     *
     * @param imageIdsToRemove the list of image IDs to remove
     */
    public void deleteImages(List<Integer> imageIdsToRemove) {
        if (imageIdsToRemove == null || imageIdsToRemove.isEmpty()) return;

        for (Integer imageId : imageIdsToRemove) {
            Optional<MediaStoreEntity> imageEntity = mediaStoreDAO.findById(imageId);
            if (imageEntity.isPresent()) {
                mediaStoreDAO.deleteById(imageId);
            }
        }
    }

    /**
     * Deletes a single image by its ID.
     *
     * @param imageId the ID of the image to remove
     */
    public void deleteImage(Integer imageId) {
        if (imageId == null) return;

        Optional<MediaStoreEntity> imageEntity = mediaStoreDAO.findById(imageId);
        if (imageEntity.isPresent()) {
            mediaStoreDAO.deleteById(imageId);
        }
    }

    /**
     * Deletes all images associated with a specific object.
     *
     * @param itemId the ID of the item
     * @param objectType the type of the object
     */
    public void deleteAllImagesForObject(Integer itemId, ObjectType objectType) {
        if (itemId == null || objectType == null) return;

        List<MediaStoreEntity> images = mediaStoreDAO.findByItemIdAndItemCategory(itemId, objectType);
        if (images != null && !images.isEmpty()) {
            mediaStoreDAO.deleteAll(images);
        }
    }

    /**
     * Deletes the thumbnail image for a specific object.
     *
     * @param itemId the ID of the item
     * @param objectType the type of the object
     */
    public void deleteThumbnailImage(Integer itemId, ObjectType objectType) {
        if (itemId == null || objectType == null) return;

        mediaStoreDAO.deleteByItemIdAndItemCategoryAndIsThumbnailTrue(itemId, objectType);
    }
}

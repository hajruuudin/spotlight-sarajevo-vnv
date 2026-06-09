package com.spotlightsarajevo.modules.media.service;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.exceptions.MediaExceptions;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import com.spotlightsarajevo.modules.media.domain.MediaStoreDAO;
import com.spotlightsarajevo.modules.media.domain.entity.MediaStoreEntity;
import com.spotlightsarajevo.modules.media.mapper.MediaStoreMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
@AllArgsConstructor
public class MediaServiceImpl implements MediaService {
    MediaStoreDAO mediaStoreDAO;
    MediaStoreMapper mediaStoreMapper;

    @Override
    public <T> void lookupThumbnailImage(T objectModel, ObjectType objectType, Integer objectId) {
        if (objectModel == null || objectType == null || objectId == null) {
            return;
        }

        MediaStoreEntity thumbnailEntity = mediaStoreDAO.findThumbnailByItemIdAndItemCategory(objectId, objectType);

        if (thumbnailEntity != null) {
            MediaStoreModel thumbnailModel = mediaStoreMapper.entityToDto(thumbnailEntity);
            try {
                // Try to set as MediaStoreModel first (for overview models)
                Method setThumbnailMethod = objectModel.getClass().getMethod("setThumbnailImage", MediaStoreModel.class);
                setThumbnailMethod.invoke(objectModel, thumbnailModel);
            } catch (NoSuchMethodException e) {
                // Fall back to String setter (for shorthand models that only need URL)
                try {
                    Method setImageURLMethod = objectModel.getClass().getMethod("setThumbnailImage", String.class);
                    setImageURLMethod.invoke(objectModel, thumbnailEntity.getImageUrl());
                } catch (NoSuchMethodException ex) {
                    System.err.println("Object of type " + objectModel.getClass().getName() + " does not have a setThumbnailImage method.");
                } catch (Exception ex) {
                    System.err.println("Error invoking setThumbnailImage method on " + objectModel.getClass().getName() + ": " + ex.getMessage());
                }
            } catch (Exception e) {
                System.err.println("Error invoking setThumbnailImage method on " + objectModel.getClass().getName() + ": " + e.getMessage());
            }
        } else {
            // Set fallback image as MediaStoreModel with no delete URL
            MediaStoreModel fallbackModel = new MediaStoreModel();
            fallbackModel.setImageUrl("https://i.ibb.co/W472hxLk/FALLBACK.png");
            fallbackModel.setImageDeleteUrl(null);
            fallbackModel.setIsThumbnail(true);

            try {
                // Try to set as MediaStoreModel first (for overview models)
                Method setThumbnailMethod = objectModel.getClass().getMethod("setThumbnailImage", MediaStoreModel.class);
                setThumbnailMethod.invoke(objectModel, fallbackModel);
            } catch (NoSuchMethodException e) {
                // Fall back to String setter (for shorthand models that only need URL)
                try {
                    Method setImageURLMethod = objectModel.getClass().getMethod("setThumbnailImage", String.class);
                    setImageURLMethod.invoke(objectModel, fallbackModel.getImageUrl());
                } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException ex) {
                    // Ignore - model might not have either method
                }
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    @Override
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

    @Override
    public ResponseEntity<MediaStoreModel> create(MediaStoreCreateModel request) {
        if(request == null) throw new MediaExceptions.MediaCreateException(ExceptionCodes.MediaExceptionCodes.MEDIA_C_BR);

        MediaStoreEntity entity = mediaStoreMapper.dtoToEntity(request);
        MediaStoreEntity addedEntity = mediaStoreDAO.save(entity);

        return ResponseEntity.status(200).body(mediaStoreMapper.entityToDto(addedEntity));
    }
}

package com.spotlightsarajevo.modules.collection.utils;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.exceptions.CollectionExceptions;
import com.spotlightsarajevo.common.utils.CollectionItem;
import com.spotlightsarajevo.modules.collection.domain.CollectionDAO;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEntity;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEventEntity;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionSpotEntity;
import com.spotlightsarajevo.modules.event.facades.EventCollectionFacade;
import com.spotlightsarajevo.modules.spot.facades.SpotCollectionFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class CollectionUtils {
    private final SpotCollectionFacade spotFacade;
    private final EventCollectionFacade eventFacade;

    public void checkAuthentication(Integer collectionUserId, Integer userId){
        if(!Objects.equals(collectionUserId, userId)){
            throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_UNAUTHORISED_ACCESS);
        }
    }

    public CollectionItem resolveCollectionItem(CollectionItem entity) {
        if (entity instanceof CollectionSpotEntity spotEntity) {
            return spotFacade.getSpotShorthand(spotEntity.getSpotId());
        }

        if (entity instanceof CollectionEventEntity eventEntity) {
            return eventFacade.getEventShorthand(eventEntity.getEventId());
        }

        throw new CollectionExceptions.CollectionSystemException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_SYSTEM_EXCEPTION);
    }

    public Integer getSystemCollectionId(Integer userId, String type, CollectionDAO collectionDAO) {
        return collectionDAO.findByUserIdAndCollectionTypeAndIsSystemTrue(userId, type)
                .map(CollectionEntity::getId)
                .orElseThrow(() -> new CollectionExceptions.CollectionNotFoundException(
                        ExceptionCodes.CollectionExceptionCodes.COLLECTION_NOT_FOUND));
    }
}

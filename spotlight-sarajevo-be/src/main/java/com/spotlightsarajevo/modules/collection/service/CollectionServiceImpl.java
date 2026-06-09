package com.spotlightsarajevo.modules.collection.service;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.enums.ProtectedKeywords;
import com.spotlightsarajevo.common.exceptions.CollectionExceptions;
import com.spotlightsarajevo.common.utils.CollectionItem;
import com.spotlightsarajevo.common.utils.CommonFunctions;
import com.spotlightsarajevo.modules.collection.api.dto.*;
import com.spotlightsarajevo.modules.collection.domain.CollectionDAO;
import com.spotlightsarajevo.modules.collection.domain.CollectionEventDAO;
import com.spotlightsarajevo.modules.collection.domain.CollectionSpotDAO;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEntity;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEventEntity;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionSpotEntity;
import com.spotlightsarajevo.modules.collection.mapper.CollectionMapper;
import com.spotlightsarajevo.modules.collection.utils.CollectionUtils;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    CollectionDAO collectionDAO;
    CollectionSpotDAO collectionSpotDAO;
    CollectionEventDAO collectionEventDAO;
    CollectionMapper collectionMapper;
    CollectionUtils collectionUtils;
    CommonFunctions commonFunctions;

    @Override
    public ResponseEntity<List<CollectionModel>> findUserCollections(Principal principal) {
        if(principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        List<CollectionEntity> entities = collectionDAO.findByUserId(user.getId());
        List<CollectionModel> models = collectionMapper.entitiesToDtos(entities);

        return ResponseEntity.status(200).body(models);
    }

    @Override
    public ResponseEntity<CollectionModel> addCollection(CollectionCreateModel request, Principal principal) {
        if(principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);
        if(request == null) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);
        if(!request.getCollectionType().equals(ObjectType.SPOT.toString()) && !request.getCollectionType().equals(ObjectType.EVENT.toString())) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);
        if(request.getCollectionName().equals(ProtectedKeywords.ALL_SPOTS.toString()) || request.getCollectionName().equals(ProtectedKeywords.ALL_EVENTS.toString())) throw new CollectionExceptions.CollectionNamingException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_NAME_CONFLICT);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        CollectionEntity entity = collectionMapper.dtoToEntity(request);

        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(user.getUsername());
        entity.setUserId(user.getId());
        entity.setIsSystem(false);

        CollectionEntity savedEntity = collectionDAO.save(entity);

        return ResponseEntity.status(200).body(collectionMapper.entityToDto(savedEntity));
    }

    @Override
    public ResponseEntity<CollectionModel> updateCollection(CollectionUpdateModel request, Principal principal) {
        if(principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);
        if(request == null) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);
        if(request.getCollectionName().equals(ProtectedKeywords.ALL_SPOTS.toString()) || request.getCollectionName().equals(ProtectedKeywords.ALL_EVENTS.toString())) throw new CollectionExceptions.CollectionNamingException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_NAME_CONFLICT);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        Optional<CollectionEntity> entity = collectionDAO.findById(request.getId());
        if (entity.isPresent()){
            if(entity.get().getIsSystem()){
                throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_SYSTEM_EXCEPTION);
            }
            collectionMapper.updateEntityFromDto(request, entity.get());
            collectionDAO.save(entity.get());

            return ResponseEntity.status(200).body(collectionMapper.entityToDto(entity.get()));
        } else {
            throw new CollectionExceptions.CollectionNotFoundException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<CollectionModel> deleteCollection(Integer collectionId, Principal principal) {
        if(principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);
        if(collectionId == null) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        Optional<CollectionEntity> entity = collectionDAO.findById(collectionId);
        if(entity.isPresent()){
            if(entity.get().getIsSystem()){
                throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_SYSTEM_EXCEPTION);
            }
            collectionUtils.checkAuthentication(entity.get().getUserId(), user.getId());
            collectionDAO.delete(entity.get());

            return ResponseEntity.status(200).body(collectionMapper.entityToDto(entity.get()));
        } else {
            throw new CollectionExceptions.CollectionNotFoundException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<CollectionItemsModel> findCollectionItems(Integer collectionId, Principal principal) {
        if(principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);
        if(collectionId == null) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        Optional<CollectionEntity> collectionEntity = collectionDAO.findById(collectionId);

        if(collectionEntity.isPresent()){
            if(Objects.equals(collectionEntity.get().getCollectionType(), ObjectType.SPOT.toString())){
                List<CollectionItem> collectionSpotLinks = collectionSpotDAO.findAllByCollectionId(collectionId);
                List<CollectionItem> collectionSpotShorthands = new ArrayList<>();

                for(CollectionItem entity : collectionSpotLinks){
                    CollectionItem item = collectionUtils.resolveCollectionItem((CollectionSpotEntity) entity);
                    collectionSpotShorthands.add(item);
                }

                CollectionItemsModel collectionItemsModel = collectionMapper.entityToCollectionItems(collectionEntity.get());
                collectionItemsModel.setCollectionItems(collectionSpotShorthands);

                return ResponseEntity.status(200).body(collectionItemsModel);
            } else if (Objects.equals(collectionEntity.get().getCollectionType(), ObjectType.EVENT.toString())){
                List<CollectionItem> collectionEventLinks = collectionEventDAO.findAllByCollectionId(collectionId);
                List<CollectionItem> collectionEventShorthands = new ArrayList<>();

                for(CollectionItem entity : collectionEventLinks){
                    CollectionItem item = collectionUtils.resolveCollectionItem((CollectionEventEntity) entity);
                    collectionEventShorthands.add(item);
                }

                CollectionItemsModel collectionItemsModel = collectionMapper.entityToCollectionItems(collectionEntity.get());
                collectionItemsModel.setCollectionItems(collectionEventShorthands);

                collectionItemsModel.setCollectionItems(collectionEventShorthands);

                return ResponseEntity.status(200).body(collectionItemsModel);
            } else {
                throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_INVALID_TYPE);
            }
        } else {
            throw new CollectionExceptions.CollectionNotFoundException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<CollectionItemsModel> findAllSpotsCollection(Principal principal) {
        if (principal == null)
            throw new CollectionExceptions.CollectionUnauthorisedException(
                    ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED
            );

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        List<CollectionSpotEntity> userSpots = collectionSpotDAO.findAllByUserId(user.getId());

        List<CollectionItem> collectionItems = new ArrayList<>();
        for (CollectionSpotEntity entity : userSpots) {
            CollectionItem item = collectionUtils.resolveCollectionItem(entity);
            collectionItems.add(item);
        }

        CollectionItemsModel collectionItemsModel = new CollectionItemsModel();
        collectionItemsModel.setCollectionId(null);
        collectionItemsModel.setCollectionType(ObjectType.SPOT.toString());
        collectionItemsModel.setCollectionItems(collectionItems);
        collectionItemsModel.setIsSystem(true);

        return ResponseEntity.status(200).body(collectionItemsModel);
    }

    @Override
    public ResponseEntity<CollectionItemsModel> findAllEventsCollection(Principal principal) {
        if (principal == null)
            throw new CollectionExceptions.CollectionUnauthorisedException(
                    ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED
            );

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        List<CollectionEventEntity> userEvents = collectionEventDAO.findAllByUserId(user.getId());

        List<CollectionItem> collectionItems = new ArrayList<>();
        for (CollectionEventEntity entity : userEvents) {
            CollectionItem item = collectionUtils.resolveCollectionItem(entity);
            collectionItems.add(item);
        }

        CollectionItemsModel collectionItemsModel = new CollectionItemsModel();
        collectionItemsModel.setCollectionId(null);
        collectionItemsModel.setCollectionType(ObjectType.EVENT.toString());
        collectionItemsModel.setCollectionItems(collectionItems);
        collectionItemsModel.setIsSystem(true);

        return ResponseEntity.status(200).body(collectionItemsModel);
    }


    @Override
    public ResponseEntity<CollectionItemsModel> addItemToCollection(CollectionAddItemModel request, Principal principal) {
        if(principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);
        if(request == null) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        Integer targetId = request.getIsSystem()
                ? collectionUtils.getSystemCollectionId(user.getId(), request.getObjectType(), collectionDAO)
                : request.getCollectionId();

        CollectionEntity collectionEntity = collectionDAO.findById(targetId)
                .orElseThrow(() -> new CollectionExceptions.CollectionNotFoundException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_NOT_FOUND));

        if (collectionEntity.getIsSystem() != request.getIsSystem()){
            throw new CollectionExceptions.CollectionSystemException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_UNAUTHORISED_ACCESS);
        }

        collectionUtils.checkAuthentication(collectionEntity.getUserId(), user.getId());

        String collectionType = collectionEntity.getCollectionType();

        if (Objects.equals(collectionType, ObjectType.SPOT.toString())){
            CollectionSpotEntity spotEntity = collectionMapper.itemSpotDtoToEntity(request);
            spotEntity.setCollectionId(collectionEntity.getId());
            spotEntity.setUserId(user.getId());
            collectionSpotDAO.save(spotEntity);

            List<CollectionItem> collectionSpotEntities = collectionSpotDAO.findAllByCollectionId(collectionEntity.getId());

            CollectionItemsModel collectionItemsModel = collectionMapper.entityToCollectionItems(collectionEntity);
            collectionItemsModel.setCollectionItems(collectionSpotEntities);

            return ResponseEntity.status(200).body(collectionItemsModel);
        } else if (Objects.equals(collectionType, ObjectType.EVENT.toString())){
            CollectionEventEntity eventEntity = collectionMapper.itemEvenDtoToEntity(request);
            eventEntity.setCollectionId(collectionEntity.getId());
            eventEntity.setUserId(user.getId());
            collectionEventDAO.save(eventEntity);

            List<CollectionItem> collectionEventEntities = collectionEventDAO.findAllByCollectionId(collectionEntity.getId());

            CollectionItemsModel collectionItemsModel = collectionMapper.entityToCollectionItems(collectionEntity);
            collectionItemsModel.setCollectionItems(collectionEventEntities);

            return ResponseEntity.status(200).body(collectionItemsModel);
        } else {
            throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_INVALID_TYPE);
        }
    }

    @Override
    public ResponseEntity<CollectionItemsModel> removeItemFromCollection(Integer collectionId, Integer itemId, String collectionType, Boolean isSystem, Principal principal) {
        if(principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);
        if(collectionId == null || itemId == null || collectionType == null || isSystem == null)
            throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);
        if (!collectionType.equals(ObjectType.SPOT.toString()) && !collectionType.equals(ObjectType.EVENT.toString()))
            throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_INVALID_TYPE);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        Integer targetId = isSystem
                ? collectionUtils.getSystemCollectionId(user.getId(), collectionType, collectionDAO)
                : collectionId;

        CollectionEntity collectionEntity = collectionDAO.findById(targetId)
                .orElseThrow(() -> new CollectionExceptions.CollectionNotFoundException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_NOT_FOUND));

        if (collectionEntity.getIsSystem() != isSystem) {
            throw new CollectionExceptions.CollectionSystemException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_UNAUTHORISED_ACCESS);
        }

        collectionUtils.checkAuthentication(collectionEntity.getUserId(), user.getId());

        if (Objects.equals(collectionType, ObjectType.SPOT.toString())){
            Optional<CollectionSpotEntity> itemToRemove = collectionSpotDAO.findByCollectionIdAndSpotId(collectionEntity.getId(), itemId);

            if (itemToRemove.isPresent()) {
                collectionSpotDAO.delete(itemToRemove.get());
            } else {
                throw new CollectionExceptions.CollectionItemNotFoundException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_ITEM_NOT_FOUND);
            }

            List<CollectionItem> collectionSpotEntities = collectionSpotDAO.findAllByCollectionId(collectionEntity.getId());
            CollectionItemsModel collectionItemsModel = new CollectionItemsModel();
            collectionItemsModel.setCollectionId(collectionEntity.getId());
            collectionItemsModel.setCollectionType(collectionType);
            collectionItemsModel.setCollectionItems(collectionSpotEntities);

            return ResponseEntity.status(200).body(collectionItemsModel);

        } else if (Objects.equals(collectionType, ObjectType.EVENT.toString())){
            Optional<CollectionEventEntity> itemToRemove = collectionEventDAO.findByCollectionIdAndEventId(collectionEntity.getId(), itemId);

            if (itemToRemove.isPresent()) {
                collectionEventDAO.delete(itemToRemove.get());
            } else {
                throw new CollectionExceptions.CollectionItemNotFoundException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_ITEM_NOT_FOUND);
            }

            List<CollectionItem> collectionEventEntities = collectionEventDAO.findAllByCollectionId(collectionEntity.getId());
            CollectionItemsModel collectionItemsModel = new CollectionItemsModel();
            collectionItemsModel.setCollectionId(collectionEntity.getId());
            collectionItemsModel.setCollectionType(collectionType);
            collectionItemsModel.setCollectionItems(collectionEventEntities);

            return ResponseEntity.status(200).body(collectionItemsModel);
        } else {
            throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_INVALID_TYPE);
        }
    }

    @Override
    public ResponseEntity<SpotInCollectionModel> findSpotInCollections(Integer spotId, Principal principal) {
        if(principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);
        if(spotId == null) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        List<CollectionSpotEntity> mappings = collectionSpotDAO.findAllBySpotIdAndUserId(spotId, user.getId());

        SpotInCollectionModel model = new SpotInCollectionModel();

        model.setIds(mappings.stream()
                .map(CollectionSpotEntity::getCollectionId)
                .toList());

        return ResponseEntity.status(200).body(model);
    }

    @Override
    public ResponseEntity<EventInCollectionModel> findEventInCollections(Integer eventId, Principal principal) {
        if(principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);
        if(eventId == null) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        List<CollectionEventEntity> mappings = collectionEventDAO.findAllByEventIdAndUserId(eventId, user.getId());

        EventInCollectionModel model = new EventInCollectionModel();

        model.setIds(mappings.stream()
                .map(CollectionEventEntity::getCollectionId)
                .toList());

        return ResponseEntity.status(200).body(model);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> addItemToCollectionsBulk(AddItemToCollectionsModel request, Principal principal) {
        if (principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);
        if (request == null || request.getIds() == null) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);
        if (request.getIsSystem()) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_UNAUTHORISED_ACCESS);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);
        String type = request.getCollectionType();
        Integer objId = request.getObjectId();

        for (Integer colId : request.getIds()) {
            Optional<CollectionEntity> collection = collectionDAO.findById(colId);

            if(collection.isPresent()){

                collectionUtils.checkAuthentication(collection.get().getUserId(), user.getId());
                if(collection.get().getIsSystem()) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_UNAUTHORISED_ACCESS);

                if (ObjectType.SPOT.toString().equals(type)) {
                    if (collectionSpotDAO.findByCollectionIdAndSpotId(colId, objId).isEmpty()) {
                        CollectionSpotEntity spotEntity = new CollectionSpotEntity();
                        spotEntity.setCollectionId(colId);
                        spotEntity.setSpotId(objId);
                        spotEntity.setUserId(user.getId());
                        collectionSpotDAO.save(spotEntity);
                    }
                } else if (ObjectType.EVENT.toString().equals(type)) {
                    if (collectionEventDAO.findByCollectionIdAndEventId(colId, objId).isEmpty()) {
                        CollectionEventEntity eventEntity = new CollectionEventEntity();
                        eventEntity.setCollectionId(colId);
                        eventEntity.setEventId(objId);
                        eventEntity.setUserId(user.getId());
                        collectionEventDAO.save(eventEntity);
                    }
                }else {
                    throw new CollectionExceptions.CollectionNotFoundException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_NOT_FOUND);
                }
            }
        }

        return ResponseEntity.status(200).body(Map.of(
                "Item added", request.getIds()
        ));
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> removeItemsFromCollectionsBulk(List<Integer> ids, String objectType, Integer objectId, Boolean isSystem, Principal principal) {
        if (principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_FIND_UNAUTHORISED);
        if (ids == null || objectId == null) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);
        if (isSystem) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_UNAUTHORISED_ACCESS);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        for (Integer colId : ids) {
            Optional<CollectionEntity> collection = collectionDAO.findById(colId);

            if(collection.isPresent()){
                collectionUtils.checkAuthentication(collection.get().getUserId(), user.getId());
                if(collection.get().getIsSystem()) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_UNAUTHORISED_ACCESS);

                if (ObjectType.SPOT.toString().equals(objectType)) {
                    collectionSpotDAO.findByCollectionIdAndSpotId(colId, objectId)
                            .ifPresent(collectionSpotDAO::delete);
                } else if (ObjectType.EVENT.toString().equals(objectType)) {
                    collectionEventDAO.findByCollectionIdAndEventId(colId, objectId)
                            .ifPresent(collectionEventDAO::delete);
                }
            } else {
                throw new CollectionExceptions.CollectionNotFoundException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_NOT_FOUND);
            }
        }

        return ResponseEntity.status(200).body(Map.of(
                "Item removed", ids
        ));
    }

    public boolean isItemSavedInSystemCollection(Integer objectId, String type, Principal principal) {
        if (principal == null) throw new CollectionExceptions.CollectionUnauthorisedException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_UNAUTHORISED_ACCESS);
        if (objectId == null || type == null) throw new CollectionExceptions.CollectionRequestContentException(ExceptionCodes.CollectionExceptionCodes.COLLECTION_REQUEST_INVALID);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);

        Integer systemCollectionId = collectionUtils.getSystemCollectionId(
                user.getId(),
                type,
                collectionDAO
        );

        if (type.equals(ObjectType.SPOT.toString())) {
            return collectionSpotDAO.existsByCollectionIdAndSpotId(systemCollectionId, objectId);
        } else if (type.equals(ObjectType.EVENT.toString())) {
            return collectionEventDAO.existsByCollectionIdAndEventId(systemCollectionId, objectId);
        }

        return false;
    }
}

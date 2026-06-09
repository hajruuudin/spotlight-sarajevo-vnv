package com.spotlightsarajevo.modules.collection.mapper;

import com.spotlightsarajevo.modules.collection.api.dto.*;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEntity;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEventEntity;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionSpotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CollectionMapper {
    List<CollectionModel> entitiesToDtos(List<CollectionEntity> entities);
    CollectionEntity dtoToEntity(CollectionCreateModel dto);
    CollectionModel entityToDto(CollectionEntity entity);
    void updateEntityFromDto(CollectionUpdateModel dto, @MappingTarget CollectionEntity entity);

    @Mapping(source = "objectId", target = "spotId")
    @Mapping(source = "collectionId", target = "collectionId")
    CollectionSpotEntity itemSpotDtoToEntity(CollectionAddItemModel dto);

    @Mapping(source = "objectId", target = "eventId")
    @Mapping(source = "collectionId", target = "collectionId")
    CollectionEventEntity itemEvenDtoToEntity(CollectionAddItemModel dto);

    @Mapping(source = "id", target = "collectionId")
    CollectionItemsModel entityToCollectionItems(CollectionEntity entity);
}

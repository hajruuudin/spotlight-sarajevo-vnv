package com.spotlightsarajevo.modules.media.mapper;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import com.spotlightsarajevo.modules.media.domain.entity.MediaStoreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MediaStoreMapper {
    MediaStoreEntity dtoToEntity(MediaStoreCreateModel dto);
    MediaStoreModel entityToDto(MediaStoreEntity entity);
    List<MediaStoreEntity> entitiesToDtos(List<MediaStoreCreateModel> dtos);
    List<MediaStoreModel> entitiesToModels(List<MediaStoreEntity> entities);
}

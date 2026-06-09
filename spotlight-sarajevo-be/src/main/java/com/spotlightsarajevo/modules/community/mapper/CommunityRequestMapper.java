package com.spotlightsarajevo.modules.community.mapper;

import com.spotlightsarajevo.modules.community.api.dto.*;
import com.spotlightsarajevo.modules.community.domain.entity.CommunityRequestDataEntity;
import com.spotlightsarajevo.modules.community.domain.entity.CommunityRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommunityRequestMapper {
    CommunityRequestEntity createDtoToEntity(CommunityRequestCreateModel dto);
    CommunityRequestModel entityToDto(CommunityRequestEntity entity);
    List<CommunityRequestModel> entitiesToDtos(List<CommunityRequestEntity> entities);
    UserCommunityRequestModel entityToUserDto(CommunityRequestEntity entity);
    List<UserCommunityRequestModel> entitiesToUserDtos(List<CommunityRequestEntity> entities);
    CommunityRequestDataModel dataEntityToDto(CommunityRequestDataEntity entity);
    CommunityRequestOverviewModel entityToOverviewDto(CommunityRequestEntity entity);
}

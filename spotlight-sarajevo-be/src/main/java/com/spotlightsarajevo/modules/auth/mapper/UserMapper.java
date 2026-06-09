package com.spotlightsarajevo.modules.auth.mapper;

import com.spotlightsarajevo.modules.auth.api.dto.*;
import com.spotlightsarajevo.modules.auth.domain.entity.UserAttendedEventsEntity;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.auth.domain.entity.UserVisitedSpotsEntity;
import com.spotlightsarajevo.modules.auth.utils.UserInfoUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserModel entityToDto(UserEntity entity);

    @Mapping(target = "email", source = "googleEmail")
    LoggedUserModel entityToGoogleLoggedDto(UserEntity entity);

    @Mapping(target = "email", source = "sysEmail")
    LoggedUserModel entityToGoogleSystemDto(UserEntity entity);

    @Mapping(target = "email", source = "googleEmail")
    UserInfoModel infoEntityToGoogleDto(UserEntity entity);

    @Mapping(target = "email", source = "sysEmail")
    UserInfoModel infoEntityToSystemDto(UserEntity entity);

    UserVisitedSpotsModel visitedSpotsEntityToDto(UserVisitedSpotsEntity entity);
    UserAttendedEventsModel attendedEventsEntityToDto(UserAttendedEventsEntity entity);
}

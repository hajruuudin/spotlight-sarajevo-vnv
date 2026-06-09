package com.spotlightsarajevo.modules.spot.utils;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.exceptions.SpotExceptions;
import com.spotlightsarajevo.modules.auth.facades.UserVisitedSpotFacade;
import com.spotlightsarajevo.modules.category.domain.SpotCategoryDAO;
import com.spotlightsarajevo.modules.category.domain.entity.SpotCategoryEntity;
import com.spotlightsarajevo.modules.spot.api.dto.SpotWorkHoursModel;
import com.spotlightsarajevo.modules.spot.domain.SpotTagsDAO;
import com.spotlightsarajevo.modules.spot.domain.SpotWorkHoursDAO;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotEntity;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotTagsEntity;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotWorkHoursEntity;
import com.spotlightsarajevo.modules.spot.mapper.SpotWorkHoursMapper;
import com.spotlightsarajevo.modules.tag.domain.TagDAO;
import com.spotlightsarajevo.modules.tag.domain.entity.TagEntity;
import com.spotlightsarajevo.modules.tag.mappers.TagMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SpotUtilities {
    SpotCategoryDAO spotCategoryDAO;
    SpotTagsDAO spotTagsDAO;
    TagDAO tagDAO;
    TagMapper tagMapper;
    SpotWorkHoursDAO spotWorkHoursDAO;
    SpotWorkHoursMapper spotWorkHoursMapper;
    UserVisitedSpotFacade userVisitedSpotFacade;

    public void setSpotCategoriesAndRating(SpotEntity entity){
        Optional<SpotCategoryEntity> category = spotCategoryDAO.findById(entity.getCategoryId());
        BigDecimal combinedReview = entity.getReviewStats().getCombinedRating();

        if (category.isPresent()){
            entity.setCategoryNameBs(category.get().getSpotCategoryNameBs());
            entity.setCategoryNameEn(category.get().getSpotCategoryNameEn());
        }
        entity.setCombinedRating(combinedReview);
    }

    public void setSpotTags(SpotEntity entity){
        List<TagEntity> tagEntities = new ArrayList<>();
        List<SpotTagsEntity> spotTags = spotTagsDAO.findBySpotId(entity.getId());

        for(SpotTagsEntity spotTag : spotTags){
            Optional<TagEntity> tag = tagDAO.findById(spotTag.getTagId());

            if(tag.isPresent()){
                tagEntities.add(tag.get());
            } else {
                // Do some logging or something
            }
        }

        entity.setSpotTags(tagMapper.entitiesToDtos(tagEntities));
    }

    public void setSpotWorkHours(SpotEntity entity){
        if (entity == null){
            throw new SpotExceptions.SpotNotFoundException(ExceptionCodes.SpotExceptionCodes.SPOT_NOT_FOUND);
        } else {
            List<SpotWorkHoursEntity> workHoursEntities = spotWorkHoursDAO.findAllBySpotId(entity.getId());
            List<SpotWorkHoursModel> workHoursModels = spotWorkHoursMapper.entitiesToDtos(workHoursEntities);

            entity.setWorkHours(workHoursModels);
        }
    }

    public boolean checkIfUserVisitedSpot(Integer spotId, Integer userId){
        return userVisitedSpotFacade.checkIfUserVisitedSpot(userId, spotId);
    }
}



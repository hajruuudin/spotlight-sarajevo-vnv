package com.spotlightsarajevo.modules.guide.utils;

import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.modules.category.domain.entity.TouristGuideCategoryEntity;
import com.spotlightsarajevo.modules.guide.api.dto.TouristGuideSectionCreateModel;
import com.spotlightsarajevo.modules.guide.api.dto.TouristGuideSectionUpdateModel;
import com.spotlightsarajevo.modules.guide.domain.TouristGuideCategoryDAO;
import com.spotlightsarajevo.modules.guide.domain.TouristGuideSectionDAO;
import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideEntity;
import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideSectionEntity;
import com.spotlightsarajevo.modules.guide.mapper.TouristGuideMapper;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import com.spotlightsarajevo.modules.media.domain.MediaStoreDAO;
import com.spotlightsarajevo.modules.media.utils.MediaUtilities;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TouristGuideUtilities {
    TouristGuideCategoryDAO touristGuideCategoryDAO;
    TouristGuideSectionDAO touristGuideSectionDAO;
    TouristGuideMapper touristGuideMapper;
    MediaUtilities mediaUtilities;
    MediaStoreDAO mediaStoreDAO;

    public void setGuideCategoryNames(TouristGuideEntity entity){
        Optional<TouristGuideCategoryEntity> category = touristGuideCategoryDAO.findById(entity.getCategoryId());

        if(category.isPresent()){
           entity.setCategoryNameEn(category.get().getCategoryNameEn());
           entity.setCategoryNameBs(category.get().getCategoryNameBs());
        }
    }

    /**
     * Creates new tourist guide sections and stores their thumbnail images.
     * @param sections list of section create models containing section data
     * @param entity the parent tourist guide entity
     */
    public void createNewSections(List<TouristGuideSectionCreateModel> sections, TouristGuideEntity entity){
        if(sections == null || sections.isEmpty()) return;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        for(TouristGuideSectionCreateModel sectionModel : sections){
            TouristGuideSectionEntity sectionEntity = touristGuideMapper.dtoToSectionEntity(sectionModel);
            sectionEntity.setGuideId(entity.getId());

            TouristGuideSectionEntity savedSection = touristGuideSectionDAO.save(sectionEntity);

            if(sectionModel.getThumbnailImage() != null){
                MediaStoreCreateModel thumbnailModel = new MediaStoreCreateModel();
                thumbnailModel.setImageUrl(sectionModel.getThumbnailImage().getImageUrl());
                mediaUtilities.updateThumbnailImage(thumbnailModel, savedSection.getId(), ObjectType.SECTION_GUIDE, username);
            }
        }
    }

    /**
     * Updates existing tourist guide sections and handles thumbnail image changes.
     * @param sections list of section update models containing updated section data
     * @param entity the parent tourist guide entity
     */
    public void updateExistingSections(List<TouristGuideSectionUpdateModel> sections, TouristGuideEntity entity){
        if(sections == null || sections.isEmpty()) return;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        for(TouristGuideSectionUpdateModel sectionModel : sections){
            Optional<TouristGuideSectionEntity> existingSection = touristGuideSectionDAO.findById(sectionModel.getId());

            if(existingSection.isPresent()){
                TouristGuideSectionEntity sectionEntity = existingSection.get();

                sectionEntity.setSectionTitleBs(sectionModel.getSectionTitleBs());
                sectionEntity.setSectionTitleEn(sectionModel.getSectionTitleEn());
                sectionEntity.setSectionBodyBs(sectionModel.getSectionBodyBs());
                sectionEntity.setSectionBodyEn(sectionModel.getSectionBodyEn());
                sectionEntity.setOrderIdx(sectionModel.getOrderIdx());

                touristGuideSectionDAO.save(sectionEntity);

                if(sectionModel.getNewThumbnailImage() != null){
                    mediaUtilities.updateThumbnailImage(sectionModel.getNewThumbnailImage(), sectionEntity.getId(), ObjectType.SECTION_GUIDE, username);
                }
            }
        }
    }

    /**
     * Deletes existing tourist guide sections and their associated thumbnail images from media store.
     * @param sectionIds list of section IDs to delete
     * @param entity the parent tourist guide entity
     */
    public void deleteExistingSections(List<Integer> sectionIds, TouristGuideEntity entity){
        if(sectionIds == null || sectionIds.isEmpty()) return;

        for(Integer sectionId : sectionIds){
            mediaStoreDAO.deleteByItemIdAndItemCategoryAndIsThumbnailTrue(sectionId, ObjectType.SECTION_GUIDE);
            touristGuideSectionDAO.deleteById(sectionId);
        }
    }
}

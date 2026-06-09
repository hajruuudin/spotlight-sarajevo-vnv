package com.spotlightsarajevo.modules.guide.service;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.exceptions.TouristGuideExceptions;
import com.spotlightsarajevo.common.specifications.TouristGuideSpecification;
import com.spotlightsarajevo.common.utils.CommonFunctions;
import com.spotlightsarajevo.modules.guide.api.dto.*;
import com.spotlightsarajevo.modules.guide.domain.TouristGuideDAO;
import com.spotlightsarajevo.modules.guide.domain.TouristGuideSectionDAO;
import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideEntity;
import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideSectionEntity;
import com.spotlightsarajevo.modules.guide.mapper.TouristGuideMapper;
import com.spotlightsarajevo.modules.guide.utils.TouristGuideUtilities;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.media.utils.MediaUtilities;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TouristGuideServiceImpl implements TouristGuideService {
    TouristGuideDAO touristGuideDAO;
    TouristGuideSectionDAO touristGuideSectionDAO;
    TouristGuideMapper touristGuideMapper;
    TouristGuideUtilities guideUtilities;
    MediaUtilities mediaUtilities;
    CommonFunctions utils;

    @Override
    public ResponseEntity<List<TouristGuideShorthandModel>> findAllGuides() {
        List<TouristGuideEntity> entities = touristGuideDAO.findAll();

        for(TouristGuideEntity entity : entities){
            guideUtilities.setGuideCategoryNames(entity);
            mediaUtilities.lookupThumbnailImage(entity, ObjectType.GUIDE_THUMBNAIL, entity.getId());
        }

        List<TouristGuideShorthandModel> models = touristGuideMapper.entitiesToShorthandDtos(entities);

        return ResponseEntity.status(200).body(models);
    }

    @Override
    public ResponseEntity<Page<TouristGuideShorthandModel>> findGuidesPaginated(PageRequest pageRequest, String searchTerm, String sortOption) {
        if (pageRequest == null) throw new TouristGuideExceptions.TouristGuideRequestException(ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_C_BR);

        PageRequest endPageRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());

        Specification<TouristGuideEntity> spec = Specification
                .allOf(TouristGuideSpecification.hasSearchTerms(searchTerm))
                .and(TouristGuideSpecification.withDynamicSorting(sortOption));

        Page<TouristGuideEntity> response = touristGuideDAO.findAll(spec, pageRequest);

        for(TouristGuideEntity entity : response.getContent()){
            guideUtilities.setGuideCategoryNames(entity);
            mediaUtilities.lookupThumbnailImage(entity, ObjectType.GUIDE_THUMBNAIL, entity.getId());
        }

        Page<TouristGuideShorthandModel> responseList = new PageImpl<>(
                touristGuideMapper.entitiesToShorthandDtos(response.getContent()),
                endPageRequest,
                response.getTotalElements()
        );

        return ResponseEntity.status(200).body(responseList);
    }


    @Override
    public ResponseEntity<List<TouristGuideShorthandModel>> findByCategory(Integer categoryId) {
        if (categoryId == null) {
            throw new TouristGuideExceptions.TouristGuideRequestException(
                    ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_BAD_REQUEST
            );
        }

        List<TouristGuideEntity> entities = touristGuideDAO.findByCategoryId(categoryId);

        for (TouristGuideEntity entity : entities) {
            guideUtilities.setGuideCategoryNames(entity);
            mediaUtilities.lookupThumbnailImage(entity, ObjectType.GUIDE_THUMBNAIL, entity.getId());
        }

        List<TouristGuideShorthandModel> models = touristGuideMapper.entitiesToShorthandDtos(entities);

        return ResponseEntity.ok(models);
    }

    @Override
    public ResponseEntity<TouristGuideOverviewModel> findGuideOverview(String slug) {
        if(slug == null) throw new TouristGuideExceptions.TouristGuideRequestException(ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_BAD_REQUEST);

        Optional<TouristGuideEntity> entityOptional = touristGuideDAO.findBySlug(slug);
        TouristGuideEntity entity = entityOptional.orElseThrow(
                () -> new TouristGuideExceptions.TouristGuideRequestException(ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_NOT_FOUND)
        );

        mediaUtilities.lookupThumbnailImage(entity, ObjectType.GUIDE_THUMBNAIL, entity.getId());
        guideUtilities.setGuideCategoryNames(entity);

        TouristGuideOverviewModel overviewModel = touristGuideMapper.entityToOverviewDto(entity);

        List<TouristGuideSectionEntity> sectionEntities = touristGuideSectionDAO.findAllByGuideId(entity.getId());
        List<TouristGuideSectionModel> sectionModels = new ArrayList<>();


        for(TouristGuideSectionEntity section : sectionEntities){
            mediaUtilities.lookupThumbnailImage(section, ObjectType.SECTION_GUIDE, section.getId());
            TouristGuideSectionModel model = touristGuideMapper.sectionEntityToDto(section);
            sectionModels.add(model);
        }

        overviewModel.setSections(sectionModels);

        return ResponseEntity.status(200).body(overviewModel);
    }

    @Override
    @Transactional
    public ResponseEntity<TouristGuideOverviewModel> create(TouristGuideCreateModel request, Principal principal) {
        if (request == null) throw new TouristGuideExceptions.TouristGuideRequestException(ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_C_BR);

        UserEntity user = utils.getUserFromPrincipal(principal);
        if (user == null || !user.getIsAdmin()) throw new TouristGuideExceptions.TouristGuideAuth(ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_C_AU);

        // Create the initial guide entity within the database
        String slug = utils.generateSlug(request.getGuideTitleEn());

        Optional<TouristGuideEntity> testEntity = touristGuideDAO.findBySlug(slug);
        if(testEntity.isPresent()) throw new TouristGuideExceptions.TouristGuideConflict(ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_C_SLUG);

        TouristGuideEntity guideEntity = touristGuideMapper.dtoToEntity(request);
        guideEntity.setSlug(slug);
        TouristGuideEntity storedEntity = touristGuideDAO.save(guideEntity);

        // Add the thumbnail image of the guide to the database
        MediaStoreCreateModel guideImageModel = new MediaStoreCreateModel(
                storedEntity.getId(),
                ObjectType.GUIDE_THUMBNAIL.toString(),
                request.getThumbnailImage().getImageUrl(),
                "EMPTY FOR NOW",
                true,
                LocalDateTime.now(),
                user.getUsername()
        );
        System.out.println(guideImageModel);
        mediaUtilities.create(guideImageModel);

        // Create the individual section objects and add their images to the database
        for (TouristGuideSectionCreateModel section : request.getSections()){
            TouristGuideSectionEntity sectionEntity = touristGuideMapper.dtoToSectionEntity(section);
            sectionEntity.setGuideId(storedEntity.getId());
            TouristGuideSectionEntity storedSectionEntity = touristGuideSectionDAO.save(sectionEntity);

            // Add the (thumbnail) image of the guide to the database
            MediaStoreCreateModel guideSectionImageModel = new MediaStoreCreateModel(
                    storedSectionEntity.getId(),
                    ObjectType.SECTION_GUIDE.toString(),
                    section.getThumbnailImage().getImageUrl(),
                    "EMPTY FOR NOW",
                    true,
                    LocalDateTime.now(),
                    user.getUsername()
            );
            mediaUtilities.create(guideSectionImageModel);
        }

        return ResponseEntity.status(200).body(touristGuideMapper.entityToOverviewDto(storedEntity));
    }

    @Override
    @Transactional
    public ResponseEntity<TouristGuideModel> update(TouristGuideUpdateModel request, Principal principal) {
        if (request == null) throw new TouristGuideExceptions.TouristGuideRequestException(ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_C_BR);

        UserEntity user = utils.getUserFromPrincipal(principal);
        if (user == null || !user.getIsAdmin()) throw new TouristGuideExceptions.TouristGuideAuth(ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_C_AU);

        TouristGuideEntity entity = touristGuideDAO.findById(request.getId())
                .orElseThrow(() ->
                        new TouristGuideExceptions.TouristGuideNotFound(
                                ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_NOT_FOUND
                        ));

        // Section 1: Updating the existing basic fields
        touristGuideMapper.updateEntityFromDto(request, entity);

        // Section 2: Updating the thumbnail image of the guide
        mediaUtilities.updateThumbnailImage(request.getNewThumbnailImage(), entity.getId(), ObjectType.GUIDE_THUMBNAIL, user.getUsername());

        // Section 3: Updating the individual sections (removing specified, creating new ones, updating existing ones)
        if(!request.getToAddSections().isEmpty()) guideUtilities.createNewSections(request.getToAddSections(), entity);
        if(!request.getToUpdateSections().isEmpty()) guideUtilities.updateExistingSections(request.getToUpdateSections(), entity);
        if(!request.getToDeleteSections().isEmpty()) guideUtilities.deleteExistingSections(request.getToDeleteSections(), entity);

        touristGuideDAO.save(entity);

        return ResponseEntity.status(200).body(touristGuideMapper.entityToDto(entity));
    }

    @Override
    public ResponseEntity<TouristGuideModel> delete(Integer id) {
        TouristGuideEntity entity = touristGuideDAO.findById(id)
                .orElseThrow(() ->
                        new TouristGuideExceptions.TouristGuideNotFound(
                                ExceptionCodes.TouristGuideExceptionCodes.TOURIST_GUIDE_NOT_FOUND
                        ));

        touristGuideDAO.delete(entity);
        return ResponseEntity.ok(touristGuideMapper.entityToDto(entity));
    }
}

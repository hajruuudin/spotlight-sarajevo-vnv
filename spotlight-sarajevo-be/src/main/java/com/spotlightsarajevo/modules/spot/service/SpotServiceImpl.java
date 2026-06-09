package com.spotlightsarajevo.modules.spot.service;

import com.spotlightsarajevo.common.exceptions.EventExceptions;
import com.spotlightsarajevo.common.utils.CommonFunctions;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.media.utils.MediaUtilities;
import com.spotlightsarajevo.modules.spot.api.dto.*;
import com.spotlightsarajevo.modules.spot.domain.SpotDAO;
import com.spotlightsarajevo.modules.spot.domain.SpotTagsDAO;
import com.spotlightsarajevo.modules.spot.domain.SpotWorkHoursDAO;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotTagsEntity;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotWorkHoursEntity;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotEntity;
import com.spotlightsarajevo.modules.spot.mapper.SpotMapper;
import com.spotlightsarajevo.modules.spot.mapper.SpotWorkHoursMapper;
import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.exceptions.SpotExceptions;
import com.spotlightsarajevo.common.specifications.SpotSpecifications;
import com.spotlightsarajevo.modules.spot.utils.SpotUtilities;
import com.spotlightsarajevo.modules.auth.domain.UserVisitedSpotsDAO;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Service
public class SpotServiceImpl implements SpotService {

    private final SpotDAO spotDAO;
    private final UserVisitedSpotsDAO userVisitedSpotsDAO;
    private final SpotTagsDAO spotTagsDAO;
    private final SpotWorkHoursDAO spotWorkHoursDAO;
    private final SpotMapper spotMapper;
    private final SpotWorkHoursMapper spotWorkHoursMapper;
    private final MediaUtilities mediaUtilities;
    private final SpotUtilities utils;
    private final CommonFunctions commonFunctions;

    /*=============================================================================*/
    /*============================== DATA RETRIEVAL ===============================*/
    /*=============================================================================*/

    @Override
    public ResponseEntity<Page<SpotModel>> findAll(PageRequest pageRequest) {
        Page<SpotEntity> page = spotDAO.findAll(pageRequest);

        List<SpotModel> models = spotMapper.entitiesToDtos(page.getContent());

        Page<SpotModel> response = new PageImpl<>(models, pageRequest, page.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Page<SpotShorthandModel>> findSpotsPaginated(PageRequest pageRequest, String searchTerm, String sortOption, List<Integer> categoryIds, BigDecimal userLatitude, BigDecimal userLongitude, Boolean excludeVisited, Principal principal) {
        if (pageRequest == null)
            throw new SpotExceptions.SpotSystemException(ExceptionCodes.SpotExceptionCodes.INVALID_REQUEST_CONTENT);

        if (categoryIds == null)
            categoryIds = new ArrayList<>();

        List<Integer> visitedSpotIds = new ArrayList<>();
        if (Boolean.TRUE.equals(excludeVisited) && principal != null) {
            UserEntity user = commonFunctions.getUserFromPrincipal(principal);
            visitedSpotIds = userVisitedSpotsDAO.findAllByUserId(user.getId())
                    .stream()
                    .map(entity -> entity.getSpotId())
                    .toList();
        }

        Specification<SpotEntity> spec = Specification
                .allOf(SpotSpecifications.hasSearchTerm(searchTerm))
                .and(SpotSpecifications.hasCategories(categoryIds))
                .and(SpotSpecifications.excludeVisitedSpots(visitedSpotIds))
                .and(SpotSpecifications.withDynamicSorting(sortOption, userLatitude, userLongitude));

        Page<SpotEntity> page = spotDAO.findAll(spec, pageRequest);

        for (SpotEntity spot : page.getContent()) {
            utils.setSpotCategoriesAndRating(spot);
            utils.setSpotTags(spot);
            mediaUtilities.lookupThumbnailImage(spot, ObjectType.SPOT, spot.getId());
        }

        Page<SpotShorthandModel> response = new PageImpl<>(
                spotMapper.entitiesToShorthandDtos(page.getContent()),
                pageRequest,
                page.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<SpotOverviewModel> findSpotOverview(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new SpotExceptions.SpotNotFoundException(
                    ExceptionCodes.SpotExceptionCodes.INVALID_REQUEST_CONTENT
            );
        }

        SpotEntity spot = spotDAO.findBySlug(slug)
                .orElseThrow(() ->
                        new SpotExceptions.SpotNotFoundException(
                                ExceptionCodes.SpotExceptionCodes.SPOT_NOT_FOUND));

        utils.setSpotCategoriesAndRating(spot);
        utils.setSpotTags(spot);
        utils.setSpotWorkHours(spot);

        SpotOverviewModel model = spotMapper.entityOverviewToDto(spot);

        mediaUtilities.lookupThumbnailImage(model, ObjectType.SPOT, spot.getId());
        mediaUtilities.lookupAllImages(model, ObjectType.SPOT, spot.getId());

        return ResponseEntity.ok(model);
    }

    @Override
    public ResponseEntity<Boolean> getSpotVisitedStatus(Integer spotId, Principal principal) {
        if (spotId == null ) throw new SpotExceptions.SpotNotFoundException(ExceptionCodes.SpotExceptionCodes.SPOT_NOT_FOUND);
        if (principal == null) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);

        Optional<SpotEntity> spotEntity = spotDAO.findById(spotId);
        UserEntity userEntity = commonFunctions.getUserFromPrincipal(principal);

        if(spotEntity.isEmpty()){
            throw new SpotExceptions.SpotNotFoundException(ExceptionCodes.SpotExceptionCodes.SPOT_NOT_FOUND);
        }

        boolean isAttended = utils.checkIfUserVisitedSpot(spotEntity.get().getId(), userEntity.getId());
        return ResponseEntity.status(200).body(isAttended);
    }

    @Override
    public ResponseEntity<List<SpotMapModel>> findSpotsForMap(String searchTerm, List<Integer> categoryIds) {
        if (categoryIds == null)
            categoryIds = new ArrayList<>();

        Specification<SpotEntity> spec = Specification
                .allOf(SpotSpecifications.hasSearchTerm(searchTerm))
                .and(SpotSpecifications.hasCategories(categoryIds));

        List<SpotEntity> spots = spotDAO.findAll(spec);

        for (SpotEntity spot : spots) {
            utils.setSpotCategoriesAndRating(spot);
            mediaUtilities.lookupThumbnailImage(spot, ObjectType.SPOT, spot.getId());
        }

        List<SpotMapModel> response = spotMapper.entitiesToMapDtos(spots);

        return ResponseEntity.ok(response);
    }

    /*=============================================================================*/
    /*======================== CREATE / UPDATE / DELETE ===========================*/
    /*=============================================================================*/

    @Override
    @Transactional
    public ResponseEntity<SpotModel> create(SpotCreateModel dto) {
        if (dto == null) throw new SpotExceptions.SpotSystemException(ExceptionCodes.SpotExceptionCodes.INVALID_REQUEST_CONTENT);

        // From Section 1: Create the spot entity from DTO
        SpotEntity entity = spotMapper.dtoToEntity(dto);
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy("Admin User");

        // Save the spot entity first to get the ID
        SpotEntity created = spotDAO.save(entity);

        // From Section 2: Add tags
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<SpotTagsEntity> tags = new ArrayList<>();
            for (Integer tagId : dto.getTagIds()) {
                SpotTagsEntity spotTag = new SpotTagsEntity();
                spotTag.setSpotId(created.getId());
                spotTag.setTagId(tagId);
                tags.add(spotTag);
            }
            spotTagsDAO.saveAllAndFlush(tags);
        }

        // From Section 3: Add work hours
        if (dto.getWorkHoursModel() != null && !dto.getWorkHoursModel().isEmpty()) {
            List<SpotWorkHoursEntity> workHours = new ArrayList<>();
            for (SpotWorkHoursModel workHoursModel : dto.getWorkHoursModel()) {
                SpotWorkHoursEntity workHoursEntity = spotWorkHoursMapper.dtoToEntity(workHoursModel);
                workHoursEntity.setSpotId(created.getId());
                workHours.add(workHoursEntity);
            }
            spotWorkHoursDAO.saveAllAndFlush(workHours);
        }

        // From Section 4: Handle thumbnail image and additional images
        mediaUtilities.updateThumbnailImage(dto.getNewThumbnailImage(), created.getId(), ObjectType.SPOT, "Admin User");
        mediaUtilities.addImages(dto.getToAddImages(), created.getId(), ObjectType.SPOT, "Admin User");

        return ResponseEntity.ok(spotMapper.entityToDto(created));
    }

    @Override
    @Transactional
    public ResponseEntity<SpotModel> update(SpotUpdateModel dto, Principal principal) {
        if (principal == null) throw new SpotExceptions.SpotUnauthorizedException(ExceptionCodes.SpotExceptionCodes.SPOT_UNAUTHORIZED_ACCESS);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);
        if (!user.getIsAdmin()) throw new SpotExceptions.SpotUnauthorizedException(ExceptionCodes.SpotExceptionCodes.SPOT_UNAUTHORIZED_ACCESS);
        if (dto == null) throw new SpotExceptions.SpotSystemException(ExceptionCodes.SpotExceptionCodes.INVALID_REQUEST_CONTENT);

        SpotEntity entity = spotDAO.findById(dto.getId())
                .orElseThrow(() ->
                        new SpotExceptions.SpotNotFoundException(
                                ExceptionCodes.SpotExceptionCodes.SPOT_NOT_FOUND));

        // From Section 1: Updating Basic Fields
        spotMapper.updateEntityFromDto(dto, entity);
        entity.setModified(LocalDateTime.now());
        entity.setModifiedBy(user.getUsername());

        // From Section 2: Update existing tags
        if (dto.getTagIds() != null) {
            spotTagsDAO.deleteAllBySpotId(entity.getId());

            List<SpotTagsEntity> newTags = new ArrayList<>();
            for (Integer tagId : dto.getTagIds()) {
                SpotTagsEntity spotTag = new SpotTagsEntity();
                spotTag.setSpotId(entity.getId());
                spotTag.setTagId(tagId);
                newTags.add(spotTag);
            }
            if (!newTags.isEmpty()) {
                spotTagsDAO.saveAllAndFlush(newTags);
            }
        }

        // From Section 3: Update Existing Work Hours
        if (dto.getWorkHoursModel() != null) {
            spotWorkHoursDAO.deleteAllBySpotId(entity.getId());

            List<SpotWorkHoursEntity> newWorkHours = new ArrayList<>();
            for (SpotWorkHoursModel workHoursModel : dto.getWorkHoursModel()) {
                SpotWorkHoursEntity workHoursEntity = spotWorkHoursMapper.dtoToEntity(workHoursModel);
                workHoursEntity.setSpotId(entity.getId());
                newWorkHours.add(workHoursEntity);
            }
            if (!newWorkHours.isEmpty()) {
                spotWorkHoursDAO.saveAllAndFlush(newWorkHours);
            }
        }

        // From Section 4: Handle updating thumbnail image and additional images
        mediaUtilities.updateThumbnailImage(dto.getNewThumbnailImage(), entity.getId(), ObjectType.SPOT, user.getUsername());
        mediaUtilities.addImages(dto.getToAddImages(), entity.getId(), ObjectType.SPOT, user.getUsername());
        mediaUtilities.deleteImages(dto.getToRemoveImages());

        spotDAO.save(entity);

        return ResponseEntity.ok(spotMapper.entityToDto(entity));
    }

    @Override
    public ResponseEntity<SpotModel> delete(Integer id) {
        SpotEntity entity = spotDAO.findById(id)
                .orElseThrow(() ->
                        new SpotExceptions.SpotNotFoundException(
                                ExceptionCodes.SpotExceptionCodes.SPOT_NOT_FOUND));

        spotDAO.delete(entity);
        return ResponseEntity.ok(spotMapper.entityToDto(entity));
    }

    @Override
    public ResponseEntity<Long> getSpotsTotalCount(Principal principal) {
        if (principal == null) throw new SpotExceptions.SpotUnauthorizedException(ExceptionCodes.SpotExceptionCodes.SPOT_UNAUTHORIZED_ACCESS);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);
        if (!user.getIsAdmin()) throw new SpotExceptions.SpotUnauthorizedException(ExceptionCodes.SpotExceptionCodes.SPOT_UNAUTHORIZED_ACCESS);

        Long count = spotDAO.countAllSpots();
        return ResponseEntity.status(200).body(count);
    }

    @Override
    public ResponseEntity<List<SpotShorthandModel>> getRecentlyAddedSpots(Integer limit, Principal principal) {
        if (principal == null) throw new SpotExceptions.SpotUnauthorizedException(ExceptionCodes.SpotExceptionCodes.SPOT_UNAUTHORIZED_ACCESS);
        if (limit == null || limit <= 0) limit = 5;

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);
        if (!user.getIsAdmin()) throw new SpotExceptions.SpotUnauthorizedException(ExceptionCodes.SpotExceptionCodes.SPOT_UNAUTHORIZED_ACCESS);

        List<SpotEntity> entities = spotDAO.findRecentlyAdded(limit);

        for(SpotEntity spot : entities){
            utils.setSpotCategoriesAndRating(spot);
            utils.setSpotTags(spot);
            mediaUtilities.lookupThumbnailImage(spot, ObjectType.SPOT, spot.getId());
        }

        List<SpotShorthandModel> models = spotMapper.entitiesToShorthandDtos(entities);
        return ResponseEntity.status(200).body(models);
    }
}

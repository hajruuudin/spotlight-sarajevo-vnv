package com.spotlightsarajevo.modules.review.service;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.exceptions.EventExceptions;
import com.spotlightsarajevo.common.exceptions.SpotExceptions;
import com.spotlightsarajevo.common.specifications.EventOrganiserSpecification;
import com.spotlightsarajevo.common.specifications.SpotReviewSpecification;
import com.spotlightsarajevo.modules.review.domain.EventOrganiserReviewDAO;
import com.spotlightsarajevo.modules.review.api.dto.*;
import com.spotlightsarajevo.modules.review.domain.SpotReviewDAO;
import com.spotlightsarajevo.modules.review.domain.entity.EventOrganiserReviewEntity;
import com.spotlightsarajevo.modules.review.mapper.EventOrganiserReviewMapper;
import com.spotlightsarajevo.modules.review.mapper.SpotReviewMapper;
import com.spotlightsarajevo.modules.review.utils.ReviewUtilities;
import com.spotlightsarajevo.modules.review.domain.entity.SpotReviewEntity;
import com.spotlightsarajevo.modules.auth.domain.UserDAO;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    SpotReviewDAO spotReviewDAO;
    SpotReviewMapper spotReviewMapper;
    EventOrganiserReviewDAO eventOrganiserReviewDAO;
    EventOrganiserReviewMapper eventOrganiserReviewMapper;
    UserDAO userDAO;
    ReviewUtilities utils;

    /* ============================================================= */
    /* ==================== SPOT REVIEWS CRUD ====================== */
    /* ============================================================= */

    /* =================== DATA RETRIEVAL METHODS ================== */

    @Override
    public ResponseEntity<Page<SpotReviewModel>> findReviewsForSpot(PageRequest pageRequest, Integer spotId, String sortOption) {
        if (spotId == null)
            throw new SpotExceptions.SpotSystemException(
                    ExceptionCodes.SpotExceptionCodes.INVALID_REQUEST_CONTENT);

        Specification<SpotReviewEntity> spec = Specification
                .allOf(SpotReviewSpecification.hasSpotId(spotId))
                .and(SpotReviewSpecification.withDynamicSorting(sortOption));

        Page<SpotReviewEntity> page = spotReviewDAO.findAll(spec, pageRequest);

        Page<SpotReviewModel> response = new PageImpl<>(
                spotReviewMapper.entitiesToDto(page.getContent()),
                pageRequest,
                page.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SpotReviewModel> findUserReview(Integer spotId, Principal principal) {
        if(spotId == null)
            throw new SpotExceptions.SpotReviewException(ExceptionCodes.SpotExceptionCodes.INVALID_REQUEST_CONTENT);

        Optional<UserEntity> userEntity = userDAO.findBySysEmailOrGoogleEmail(principal.getName());

        if(userEntity.isPresent()){
            Optional<SpotReviewEntity> spotReviewEntity = spotReviewDAO.findBySpotIdAndUserId(spotId, userEntity.get().getId());

            return spotReviewEntity
                    .map(entity -> ResponseEntity.status(200).body(spotReviewMapper.entityToDto(entity)))
                    .orElseGet(() -> ResponseEntity.status(200).body(null));
        } else {
            throw new SpotExceptions.SpotReviewException(ExceptionCodes.SpotExceptionCodes.SPOT_UNAUTHORIZED_ACCESS);
        }
    }

    /* =================== CREATE, UPDATE, DELETE METHODS  ================== */

    @Override
    public ResponseEntity<SpotReviewModel> createSpotReview(SpotReviewCreateModel dto, Principal principal) {
        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());

        if(user.isPresent()){
            SpotReviewEntity entity = spotReviewMapper.dtoToEntity(dto);
            entity.setUserId(user.get().getId());
            entity.setUsername(user.get().getUsername());
            entity.setCreated(LocalDate.now());

            SpotReviewEntity created = spotReviewDAO.save(entity);
            return ResponseEntity.ok(spotReviewMapper.entityToDto(created));
        } else {
            throw new SpotExceptions.SpotReviewException(ExceptionCodes.SpotExceptionCodes.INVALID_REQUEST_CONTENT);
        }
    }


    @Override
    public ResponseEntity<SpotReviewModel> updateSpotReview(SpotReviewUpdateModel dto, Principal principal) {
//        if (!utils.isAuthorisedUser(dto.getUserId(), principal)) {
//            throw new SpotExceptions.SpotUnauthorizedException(
//                    ExceptionCodes.SpotExceptionCodes.SPOT_UNAUTHORIZED_ACCESS);
//        } THINK ABOUT THIS LATER ON WHEN REFACTORING CODE

        SpotReviewEntity entity = spotReviewDAO.findById(dto.getId())
                .orElseThrow(() ->
                        new SpotExceptions.SpotReviewException(
                                ExceptionCodes.SpotExceptionCodes.SPOT_REVIEW_NOT_FOUND));

        spotReviewMapper.updateEntityFromDto(dto, entity);
        entity.setModified(LocalDate.now());

        spotReviewDAO.save(entity);
        return ResponseEntity.ok(spotReviewMapper.entityToDto(entity));
    }

    @Override
    public ResponseEntity<SpotReviewModel> deleteSpotReview(Integer spotId, Integer reviewId, Principal principal) {
        if (spotId == null)
            throw new SpotExceptions.SpotSystemException(
                    ExceptionCodes.SpotExceptionCodes.EMPTY_SPOT_REQUEST_BODY);

        SpotReviewEntity review = spotReviewDAO.findById(reviewId)
                .orElseThrow(() ->
                        new SpotExceptions.SpotReviewException(
                                ExceptionCodes.SpotExceptionCodes.SPOT_REVIEW_NOT_FOUND));

        if (!review.getSpotId().equals(spotId))
            throw new SpotExceptions.SpotReviewException(
                    ExceptionCodes.SpotExceptionCodes.SPOT_REVIEW_CONFLICT);

        spotReviewDAO.delete(review);
        return ResponseEntity.ok(spotReviewMapper.entityToDto(review));
    }

    /* ============================================================= */
    /* ============== EVENT ORGANISER REVIEWS CRUD ================= */
    /* ============================================================= */

    /* =================== DATA RETRIEVAL METHODS ================== */

    @Override
    public ResponseEntity<Page<EventOrganiserReviewModel>> findReviewsForOrganiser(PageRequest pageRequest, Integer organiserId, String sortOption) {
        if (organiserId == null)
            throw new SpotExceptions.SpotSystemException(
                    ExceptionCodes.SpotExceptionCodes.INVALID_REQUEST_CONTENT);

        Specification<EventOrganiserReviewEntity> spec = Specification
                .allOf(EventOrganiserSpecification.hasOrganiserId(organiserId))
                .and(EventOrganiserSpecification.withDynamicSorting(sortOption));

        Page<EventOrganiserReviewEntity> page = eventOrganiserReviewDAO.findAll(spec, pageRequest);

        Page<EventOrganiserReviewModel> response = new PageImpl<>(
                eventOrganiserReviewMapper.entitiesToDto(page.getContent()),
                pageRequest,
                page.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<EventOrganiserReviewModel> findUserOrganiserReview(Integer organiserId, Principal principal) {
        if (organiserId == null)
            throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);

        Optional<UserEntity> userEntity = userDAO.findBySysEmailOrGoogleEmail(principal.getName());

        if (userEntity.isPresent()) {
            Optional<EventOrganiserReviewEntity> reviewEntity = eventOrganiserReviewDAO.findByOrganiserIdAndUserId(organiserId, userEntity.get().getId());

            return reviewEntity
                    .map(entity -> ResponseEntity.status(200).body(eventOrganiserReviewMapper.entityToDto(entity)))
                    .orElseGet(() -> ResponseEntity.status(200).body(null));
        } else {
            throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.EVENT_ORGANISER_REVIEW_NOT_FOUND);
        }
    }

    /* =================== CREATE, UPDATE, DELETE METHODS  ================== */

    @Override
    public ResponseEntity<EventOrganiserReviewModel> createOrganiserReview(EventOrganiserReviewCreateModel dto, Principal principal) {
        Optional<UserEntity> user = userDAO.findBySysEmailOrGoogleEmail(principal.getName());

        if (user.isPresent()) {
            EventOrganiserReviewEntity entity = eventOrganiserReviewMapper.dtoToEntity(dto);

            entity.setUserId(user.get().getId());
            entity.setUsername(user.get().getUsername());
            entity.setCreated(LocalDate.now());

            System.out.println("Entity to be save id:" + entity);
            EventOrganiserReviewEntity created = eventOrganiserReviewDAO.save(entity);
            return ResponseEntity.ok(eventOrganiserReviewMapper.entityToDto(created));
        } else {
            throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);
        }
    }

    @Override
    public ResponseEntity<EventOrganiserReviewModel> updateOrganiserReview(EventOrganiserReviewUpdateModel dto, Principal principal) {
        // Validation logic
        // if (!utils.isAuthorisedUser(dto.getUserId(), principal)) { ... }

        EventOrganiserReviewEntity entity = eventOrganiserReviewDAO.findById(dto.getId())
                .orElseThrow(() ->
                        new EventExceptions.EventNotFoundException(ExceptionCodes.EventExceptionCodes.EVENT_ORGANISER_REVIEW_NOT_FOUND)); // Assuming you add a specific code for Organisers

        eventOrganiserReviewMapper.updateEntityFromDto(dto, entity);
        entity.setModified(LocalDate.now());


        eventOrganiserReviewDAO.save(entity);
        return ResponseEntity.ok(eventOrganiserReviewMapper.entityToDto(entity));
    }

    @Override
    public ResponseEntity<EventOrganiserReviewModel> deleteOrganiserReview(Integer organiserId, Integer reviewId, Principal principal) {
        if (organiserId == null)
            throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);

        EventOrganiserReviewEntity review = eventOrganiserReviewDAO.findById(reviewId)
                .orElseThrow(() ->
                        new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT));

        if (!review.getOrganiserId().equals(organiserId))
            throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);

        eventOrganiserReviewDAO.delete(review);
        return ResponseEntity.ok(eventOrganiserReviewMapper.entityToDto(review));
    }

}

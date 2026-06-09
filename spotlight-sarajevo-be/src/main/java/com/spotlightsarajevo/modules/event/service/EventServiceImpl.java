package com.spotlightsarajevo.modules.event.service;

import com.spotlightsarajevo.common.utils.CommonFunctions;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.event.api.dto.*;
import com.spotlightsarajevo.modules.event.domain.EventDAO;
import com.spotlightsarajevo.modules.event.domain.EventOrganiserDAO;
import com.spotlightsarajevo.modules.event.domain.entity.EventEntity;
import com.spotlightsarajevo.modules.event.domain.entity.EventOrganiserEntity;
import com.spotlightsarajevo.modules.event.utils.EventUtilities;
import com.spotlightsarajevo.modules.event.mapper.EventMapper;
import com.spotlightsarajevo.modules.event.mapper.EventOrganiserMapper;
import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.exceptions.EventExceptions;
import com.spotlightsarajevo.common.specifications.EventSpecification;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    EventDAO eventDAO;
    EventOrganiserDAO eventOrganiserDAO;
    EventUtilities utils;
    CommonFunctions commonFunctions;
    EventMapper eventMapper;
    EventOrganiserMapper eventOrganiserMapper;
    MediaUtilities mediaUtilities;

    @Override
    public ResponseEntity<Page<EventShorthandModel>> findEventsPaginated(PageRequest pageRequest, String searchTerm, String sortOption, List<Integer> categoryIds) {
        if(pageRequest == null){
            throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);
        }

        if(categoryIds == null){
            categoryIds = new ArrayList<>();
        }

        PageRequest endPageRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());

        Specification<EventEntity> spec = Specification
                .allOf(EventSpecification.hasSearchTerms(searchTerm))
                .and(EventSpecification.hasCategories(categoryIds))
                .and(EventSpecification.withDynamicSorting(sortOption));

        Page<EventEntity> eventEntities = eventDAO.findAll(spec, endPageRequest);

        for(EventEntity eventEntity : eventEntities.getContent()){
            utils.setEventCategories(eventEntity);
            utils.setEventTags(eventEntity);
            mediaUtilities.lookupThumbnailImage(eventEntity, ObjectType.EVENT, eventEntity.getId());
        }

        Page<EventShorthandModel> responseList = new PageImpl<>(
                eventMapper.entitiesToShorthandDtos(eventEntities.getContent()),
                endPageRequest,
                eventEntities.getTotalElements()
        );
        return ResponseEntity.status(200).body(responseList);
    }

    @Override
    @Transactional
    public ResponseEntity<EventModel> create(EventCreateModel dto, Principal principal) {
        if (principal == null) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);
        if (dto == null) throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);
        if (!user.getIsAdmin()) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);

        // From Section 1: Create the event entity from DTO
        EventEntity entity = eventMapper.dtoToEntity(dto);
        entity.setCreated(LocalDateTime.now());
        entity.setCreatedBy(user.getUsername());

        // Save the event entity first to get the ID
        EventEntity created = eventDAO.save(entity);

        // From Section 2: Add tags
        utils.addEventTags(dto.getEventTagIds(), created.getId());

        // From Section 3: Handle thumbnail image and additional images
        mediaUtilities.updateThumbnailImage(dto.getNewThumbnailImage(), created.getId(), ObjectType.EVENT, user.getUsername());
        mediaUtilities.addImages(dto.getToAddImages(), created.getId(), ObjectType.EVENT, user.getUsername());

        return ResponseEntity.ok(eventMapper.entityToDto(created));
    }

    @Override
    @Transactional
    public ResponseEntity<EventModel> update(EventUpdateModel eventUpdateModel, Principal principal) {
        if (principal == null) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);
        if (!user.getIsAdmin()) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);
        if (eventUpdateModel == null) throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);

        EventEntity entity = eventDAO.findById(eventUpdateModel.getId())
                .orElseThrow(() ->
                        new EventExceptions.EventNotFoundException(
                                ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND));

        // From Section 1: Updating Basic Fields
        eventMapper.updateEntityFromDto(eventUpdateModel, entity);
        entity.setModified(LocalDateTime.now());
        entity.setModifiedBy(user.getUsername());

        // From Section 2: Update existing tags
        utils.updateEventTagsBulk(eventUpdateModel, entity);

        // From Section 3: Update Event Information (Handled by the first mapper section info section)

        // From Section 4: Handle updating thumbnail image and additional images
        mediaUtilities.updateThumbnailImage(eventUpdateModel.getNewThumbnailImage(), entity.getId(), ObjectType.EVENT, user.getUsername());
        mediaUtilities.addImages(eventUpdateModel.getToAddImages(), entity.getId(), ObjectType.EVENT, user.getUsername());
        mediaUtilities.deleteImages(eventUpdateModel.getToRemoveImages());

        eventDAO.save(entity);

        return ResponseEntity.ok(eventMapper.entityToDto(entity));
    }

    @Override
    public ResponseEntity<List<EventOrganiserModel>> findAllOrganisers() {
        List<EventOrganiserEntity> organiserEntities = eventOrganiserDAO.findAll();
        List<EventOrganiserModel> organiserModels = eventOrganiserMapper.entitiesToDtos(organiserEntities);

        return ResponseEntity.status(200).body(organiserModels);
    }

    @Override
    @Transactional
    public ResponseEntity<EventOrganiserModel> createEventOrganiser(EventOrganiserCreateModel request, Principal principal) {
        if(request == null) throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);
        if(principal == null) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);

        UserEntity loggedUser = commonFunctions.getUserFromPrincipal(principal);
        if (!loggedUser.getIsAdmin()) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);

        EventOrganiserEntity eventOrganiser = eventOrganiserMapper.dtoToEntity(request);
        EventOrganiserEntity savedEntity = eventOrganiserDAO.save(eventOrganiser);

        MediaStoreCreateModel image = request.getNewThumbnailImage();
        image.setItemId(savedEntity.getId());

        mediaUtilities.updateThumbnailImage(image, savedEntity.getId(), ObjectType.EVENT, loggedUser.getUsername());

        return ResponseEntity.status(200).body(eventOrganiserMapper.entityToDto(savedEntity));
    }

    @Override
    @Transactional
    public ResponseEntity<EventOrganiserModel> update(EventOrganiserUpdateModel request, Principal principal) {
        if (principal == null) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);
        if (!user.getIsAdmin()) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);
        if (request == null) throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);

        EventOrganiserEntity entity = eventOrganiserDAO.findById(request.getId())
                .orElseThrow(() ->
                        new EventExceptions.EventNotFoundException(
                                ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND));

        eventOrganiserMapper.updateEntityFromDto(request, entity);

        mediaUtilities.updateThumbnailImage(request.getNewThumbnailImage(), entity.getId(), ObjectType.ORGANISER, user.getUsername());

        eventOrganiserDAO.save(entity);

        EventOrganiserModel response = eventOrganiserMapper.entityToDto(entity);
        mediaUtilities.lookupThumbnailImage(response, ObjectType.ORGANISER, entity.getId());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<EventModel> delete(Integer id) {
        EventEntity entity = eventDAO.findById(id)
                .orElseThrow(() ->
                        new EventExceptions.EventNotFoundException(
                                ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND));

        eventDAO.delete(entity);
        return ResponseEntity.ok(eventMapper.entityToDto(entity));
    }

    @Override
    public ResponseEntity<EventOverviewModel> findEventOverview(String eventSlug) {
        if(eventSlug == null || eventSlug.isBlank()){
            throw new EventExceptions.EventNotFoundException(ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND);
        }

        Optional<EventEntity> eventEntity = eventDAO.findBySlug(eventSlug);

        if(eventEntity.isEmpty()){
            throw new EventExceptions.EventNotFoundException(ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND);
        } else {
            utils.setEventCategories(eventEntity.get());
            utils.setEventTags(eventEntity.get());
            utils.setEventOrganiserAndStats(eventEntity.get());

            EventOverviewModel eventOverviewModel = eventMapper.entityToOverviewDto(eventEntity.get());

            mediaUtilities.lookupThumbnailImage(eventOverviewModel, ObjectType.EVENT, eventEntity.get().getId());
            mediaUtilities.lookupAllImages(eventOverviewModel, ObjectType.EVENT, eventEntity.get().getId());

            return ResponseEntity.status(200).body(eventOverviewModel);
        }
    }

    @Override
    public ResponseEntity<Boolean> getEventAttendedStatus(Integer eventId, Principal principal) {
        if (eventId == null ) throw new EventExceptions.EventNotFoundException(ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND);
        if (principal == null) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);

        Optional<EventEntity> eventEntity = eventDAO.findById(eventId);
        UserEntity userEntity = commonFunctions.getUserFromPrincipal(principal);

        if(eventEntity.isEmpty()){
            throw new EventExceptions.EventNotFoundException(ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND);
        }

        boolean isAttended = utils.checkIfUserAttendedEvent(eventEntity.get().getId(), userEntity.getId());
        return ResponseEntity.status(200).body(isAttended);
    }

    @Override
    public ResponseEntity<List<EventShorthandModel>> findEventsForDate(LocalDate date) {
        if (date == null) {
            throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);
        }

        Specification<EventEntity> spec = Specification
                .allOf(EventSpecification.hasDate(date));

        List<EventEntity> eventEntities = eventDAO.findAll(spec);

        for (EventEntity eventEntity : eventEntities) {
            utils.setEventCategories(eventEntity);
            utils.setEventTags(eventEntity);
            mediaUtilities.lookupThumbnailImage(eventEntity, ObjectType.EVENT, eventEntity.getId());
        }

        List<EventShorthandModel> response = eventMapper.entitiesToShorthandDtos(eventEntities);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<LocalDate, Boolean>> checkEventDatesForMonth(Integer year, Integer month) {
        if (year == null || month == null || month < 1 || month > 12) {
            throw new EventExceptions.EventSystemException(ExceptionCodes.EventExceptionCodes.INVALID_REQUEST_CONTENT);
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        Specification<EventEntity> spec = Specification
                .allOf(EventSpecification.hasDateRange(startOfMonth, endOfMonth));

        List<EventEntity> eventEntities = eventDAO.findAll(spec);

        Map<LocalDate, Boolean> result = new LinkedHashMap<>();

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate currentDate = yearMonth.atDay(day);
            boolean hasEvents = eventEntities.stream()
                    .anyMatch(event -> !event.getStartDate().toLocalDate().isAfter(currentDate)
                            && !event.getEndDate().toLocalDate().isBefore(currentDate));
            result.put(currentDate, hasEvents);
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Long> getEventsTotalCount(Principal principal) {
        if (principal == null) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);
        if (!user.getIsAdmin()) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);

        Long count = eventDAO.countAllEvents();
        return ResponseEntity.status(200).body(count);
    }

    @Override
    public ResponseEntity<List<EventShorthandModel>> getRecentlyAddedEvents(Integer limit, Principal principal) {
        if (principal == null) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);
        if (limit == null || limit <= 0) limit = 5;

        UserEntity user = commonFunctions.getUserFromPrincipal(principal);
        if (!user.getIsAdmin()) throw new EventExceptions.EventUnauthorizedException(ExceptionCodes.EventExceptionCodes.EVENT_UNAUTHORIZED_ACCESS);

        List<EventEntity> entities = eventDAO.findRecentlyAdded(limit);

        for(EventEntity eventEntity : entities){
            utils.setEventCategories(eventEntity);
            utils.setEventTags(eventEntity);
            mediaUtilities.lookupThumbnailImage(eventEntity, ObjectType.EVENT, eventEntity.getId());
        }

        List<EventShorthandModel> models = eventMapper.entitiesToShorthandDtos(entities);
        return ResponseEntity.status(200).body(models);
    }
}

package com.spotlightsarajevo.spotlight_sarajevo.unit.event;

import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.exceptions.EventExceptions;
import com.spotlightsarajevo.common.specifications.EventSpecification;
import com.spotlightsarajevo.common.utils.CommonFunctions;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.event.api.dto.*;
import com.spotlightsarajevo.modules.event.domain.EventDAO;
import com.spotlightsarajevo.modules.event.domain.EventOrganiserDAO;
import com.spotlightsarajevo.modules.event.domain.entity.EventEntity;
import com.spotlightsarajevo.modules.event.domain.entity.EventOrganiserEntity;
import com.spotlightsarajevo.modules.event.mapper.EventMapper;
import com.spotlightsarajevo.modules.event.mapper.EventOrganiserMapper;
import com.spotlightsarajevo.modules.event.service.EventServiceImpl;
import com.spotlightsarajevo.modules.event.utils.EventUtilities;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import com.spotlightsarajevo.modules.media.utils.MediaUtilities;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotEntity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.parser.Entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EventServiceUnitTests {
    @Mock
    EventDAO eventDAO;
    @Mock
    EventOrganiserDAO eventOrganiserDAO;
    @Mock
    EventUtilities utils;
    @Mock
    CommonFunctions commonFunctions;
    @Mock
    EventMapper eventMapper;
    @Mock
    EventOrganiserMapper eventOrganiserMapper;
    @Mock
    MediaUtilities mediaUtilities;
    @Mock
    Principal principal;

    UserEntity userEntity;

    @InjectMocks
    EventServiceImpl eventService;

    @BeforeEach
    void init() {
        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setIsAdmin(true);
        when(commonFunctions.getUserFromPrincipal(principal)).thenReturn(userEntity);
    }

    @Test
    void findEventsPaginated_ShouldReturnEvents_WhenRequestContentFilled() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Integer> categoryIds = new ArrayList<>();
        String searchTerm = "example";
        String sortOption = "ALPHABETICAL";

        EventEntity entity = new EventEntity();
        List<EventEntity> eventEntities = List.of(entity);
        EventShorthandModel eventShorthandModel = new EventShorthandModel();
        List<EventShorthandModel> eventShorthandModels = List.of(eventShorthandModel);
        Page<EventEntity> entityPage = new PageImpl<>(eventEntities, pageRequest, 1);

        when(eventDAO.findAll(ArgumentMatchers.any(Specification.class), eq(pageRequest))).thenReturn(entityPage);
        when(eventMapper.entitiesToShorthandDtos(entityPage.getContent())).thenReturn(eventShorthandModels);

        ResponseEntity<Page<EventShorthandModel>> result = eventService.findEventsPaginated(
                pageRequest, searchTerm, sortOption, categoryIds
        );

        assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        Assertions.assertEquals(1, result.getBody().getTotalElements());
        Assertions.assertEquals(eventShorthandModel, result.getBody().getContent().get(0));

        verify(eventDAO).findAll(ArgumentMatchers.any(Specification.class), eq(pageRequest));
        verify(utils).setEventCategories(entity);
        verify(utils).setEventTags(entity);
        verify(mediaUtilities).lookupThumbnailImage(entity, ObjectType.EVENT, entity.getId());
        verify(eventMapper).entitiesToShorthandDtos(eventEntities);
    }

    @Test
    void findEventsPaginated_ShouldThrowException_WhenPageRequestNull() {
        PageRequest pageRequest = null;
        List<Integer> categoryIds = new ArrayList<>();
        String searchTerm = "example";
        String sortOption = "ALPHABETICAL";

        assertThrows(EventExceptions.EventSystemException.class, () -> {
            eventService.findEventsPaginated(
                    pageRequest, searchTerm, sortOption, categoryIds
            );
        });
    }

    @Test
    void createEvent_ShouldCreateEvent_WhenRequestContentFilled() {
        EventCreateModel dto = new EventCreateModel();
        EventEntity entity = new EventEntity();
        EventEntity created = new EventEntity();
        created.setId(1);
        EventModel eventModel = new EventModel();

        when(eventMapper.dtoToEntity(dto)).thenReturn(entity);
        when(eventDAO.save(entity)).thenReturn(created);
        when(eventMapper.entityToDto(created)).thenReturn(eventModel);

        ResponseEntity<EventModel> result = eventService.create(
                dto, principal
        );

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());

        verify(eventDAO).save(entity);
        verify(utils).addEventTags(dto.getEventTagIds(), created.getId());
        verify(mediaUtilities).updateThumbnailImage(dto.getNewThumbnailImage(), created.getId(), ObjectType.EVENT, userEntity.getUsername());
    }

    @Test
    void createEvent_ShouldThrowException_WhenUserNotAdmin() {
        userEntity.setIsAdmin(false);
        EventCreateModel dto = new EventCreateModel();

        assertThrows(EventExceptions.EventUnauthorizedException.class, () -> {
            eventService.create(dto, principal);
        });
    }

    @Test
    void createEvent_ShouldThrowException_WhenDTONull() {
        EventCreateModel dto = null;

        assertThrows(EventExceptions.EventSystemException.class, () -> {
            eventService.create(dto, principal);
        });
    }

    @Test
    void updateEvent_ShouldUpdateEvent_WhenRequestContentFilled() {
        EventEntity foundEntity = new EventEntity();
        Optional<EventEntity> optionalEventEntity = Optional.of(foundEntity);
        EventUpdateModel eventUpdateModel = new EventUpdateModel();
        EventModel eventModel = new EventModel();

        when(eventDAO.findById(eventUpdateModel.getId())).thenReturn(optionalEventEntity);
        when(eventMapper.entityToDto(optionalEventEntity.get())).thenReturn(eventModel);

        ResponseEntity<EventModel> result = eventService.update(
                eventUpdateModel, principal
        );

        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

        verify(eventDAO).findById(eventUpdateModel.getId());
        verify(eventMapper).updateEntityFromDto(eventUpdateModel, optionalEventEntity.get());
        verify(eventDAO).save(foundEntity);
        verify(eventMapper).entityToDto(foundEntity);
        verify(utils).updateEventTagsBulk(eventUpdateModel, optionalEventEntity.get());
        verify(mediaUtilities).updateThumbnailImage(eventUpdateModel.getNewThumbnailImage(), optionalEventEntity.get().getId(), ObjectType.EVENT, userEntity.getUsername());
        verify(mediaUtilities).addImages(eventUpdateModel.getToAddImages(), optionalEventEntity.get().getId(), ObjectType.EVENT, userEntity.getUsername());
        verify(mediaUtilities).deleteImages(eventUpdateModel.getToRemoveImages());
    }

    @Test
    void updateEvent_ShouldThrowException_WhenUserNotAdmin() {
        userEntity.setIsAdmin(false);
        EventUpdateModel eventUpdateModel = new EventUpdateModel();

        assertThrows(EventExceptions.EventUnauthorizedException.class, () -> {
            eventService.update(eventUpdateModel, principal);
        });
    }

    @Test
    void updateEvent_ShouldThrowException_WhenDTONull() {
        EventUpdateModel eventUpdateModel = null;

        assertThrows(EventExceptions.EventSystemException.class, () -> {
            eventService.update(eventUpdateModel, principal);
        });
    }

    @Test
    void findAllOrganisers_ShouldReturnOrganisers() {
        List<EventOrganiserEntity> organiserEntities = new ArrayList<>();
        EventOrganiserEntity organiser = new EventOrganiserEntity();
        organiserEntities.add(organiser);
        
        List<EventOrganiserModel> organiserModels = new ArrayList<>();
        EventOrganiserModel model = new EventOrganiserModel();
        organiserModels.add(model);

        when(eventOrganiserDAO.findAll()).thenReturn(organiserEntities);
        when(eventOrganiserMapper.entitiesToDtos(organiserEntities)).thenReturn(organiserModels);

        ResponseEntity<List<EventOrganiserModel>> result = eventService.findAllOrganisers();

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(model, result.getBody().get(0));

        verify(eventOrganiserDAO).findAll();
        verify(eventOrganiserMapper).entitiesToDtos(organiserEntities);
    }

    @Test
    void createEventOrganiser_ShouldCreateEventOrganiser_WhenRequestCorrect() {
        EventOrganiserCreateModel request = new EventOrganiserCreateModel();
        MediaStoreCreateModel image = new MediaStoreCreateModel();
        request.setNewThumbnailImage(image);
        
        EventOrganiserEntity organiserEntity = new EventOrganiserEntity();
        organiserEntity.setId(1);
        
        EventOrganiserModel organiserModel = new EventOrganiserModel();

        when(eventOrganiserMapper.dtoToEntity(request)).thenReturn(organiserEntity);
        when(eventOrganiserDAO.save(organiserEntity)).thenReturn(organiserEntity);
        when(eventOrganiserMapper.entityToDto(organiserEntity)).thenReturn(organiserModel);

        ResponseEntity<EventOrganiserModel> result = eventService.createEventOrganiser(request, principal);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(organiserModel, result.getBody());

        verify(eventOrganiserDAO).save(organiserEntity);
        verify(mediaUtilities).updateThumbnailImage(image, organiserEntity.getId(), ObjectType.EVENT, userEntity.getUsername());
        verify(eventOrganiserMapper).entityToDto(organiserEntity);
    }

    @Test
    void createEventOrganiser_ShouldThrowException_WhenRequestNull() {
        EventOrganiserCreateModel request = null;

        assertThrows(EventExceptions.EventSystemException.class, () -> {
            eventService.createEventOrganiser(request, principal);
        });
    }

    @Test
    void createEventOrganiser_ShouldThrowException_WhenUserNotAdmin() {
        userEntity.setIsAdmin(false);
        EventOrganiserCreateModel request = new EventOrganiserCreateModel();

        assertThrows(EventExceptions.EventUnauthorizedException.class, () -> {
            eventService.createEventOrganiser(request, principal);
        });
    }

    @Test
    void deleteEvent_ShouldDeleteEvent_WhenIDNotNull() {
        Integer eventId = 1;
        EventEntity entity = new EventEntity();
        entity.setId(eventId);
        Optional<EventEntity> optionalEventEntity = Optional.of(entity);
        EventModel eventModel = new EventModel();

        when(eventDAO.findById(eventId)).thenReturn(optionalEventEntity);
        when(eventMapper.entityToDto(entity)).thenReturn(eventModel);

        ResponseEntity<EventModel> result = eventService.delete(eventId);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());

        verify(eventDAO).findById(eventId);
        verify(eventDAO).delete(entity);
        verify(eventMapper).entityToDto(entity);
    }

    @Test
    void deleteEvent_ShouldThrowException_WhenIDNull() {
        Integer eventId = null;

        when(eventDAO.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventExceptions.EventNotFoundException.class, () -> {
            eventService.delete(eventId);
        });
    }

    @Test
    void findEventsForDate_ShouldReturnEvents_WhenDateNotNull() {
        // Arrange
        java.time.LocalDate date = java.time.LocalDate.now();
        EventEntity entity = new EventEntity();
        List<EventEntity> eventEntities = List.of(entity);
        EventShorthandModel shorthandModel = new EventShorthandModel();
        List<EventShorthandModel> shorthandModels = List.of(shorthandModel);

        when(eventDAO.findAll(ArgumentMatchers.any(Specification.class))).thenReturn(eventEntities);
        when(eventMapper.entitiesToShorthandDtos(eventEntities)).thenReturn(shorthandModels);

        // Act
        ResponseEntity<List<EventShorthandModel>> result = eventService.findEventsForDate(date);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(shorthandModel, result.getBody().get(0));

        verify(eventDAO).findAll(ArgumentMatchers.any(Specification.class));
        verify(utils).setEventCategories(entity);
        verify(utils).setEventTags(entity);
        verify(mediaUtilities).lookupThumbnailImage(entity, ObjectType.EVENT, entity.getId());
        verify(eventMapper).entitiesToShorthandDtos(eventEntities);
    }

    @Test
    void getRecentlyAddedEvents_ShouldReturnEvents_WhenUserIsAdmin() {
        // Arrange
        Integer limit = 5;
        EventEntity entity = new EventEntity();
        entity.setId(1);
        List<EventEntity> entities = List.of(entity);
        EventShorthandModel shorthandModel = new EventShorthandModel();
        List<EventShorthandModel> shorthandModels = List.of(shorthandModel);

        when(eventDAO.findRecentlyAdded(limit)).thenReturn(entities);
        when(eventMapper.entitiesToShorthandDtos(entities)).thenReturn(shorthandModels);

        // Act
        ResponseEntity<List<EventShorthandModel>> result = eventService.getRecentlyAddedEvents(limit, principal);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(shorthandModel, result.getBody().get(0));

        verify(eventDAO).findRecentlyAdded(limit);
        verify(utils).setEventCategories(entity);
        verify(utils).setEventTags(entity);
        verify(mediaUtilities).lookupThumbnailImage(entity, ObjectType.EVENT, entity.getId());
        verify(eventMapper).entitiesToShorthandDtos(entities);
    }
}

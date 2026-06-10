package com.spotlightsarajevo.spotlight_sarajevo.unit.spot;

import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.exceptions.EventExceptions;
import com.spotlightsarajevo.common.exceptions.SpotExceptions;
import com.spotlightsarajevo.common.utils.CommonFunctions;
import com.spotlightsarajevo.modules.auth.domain.UserVisitedSpotsDAO;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.auth.domain.entity.UserVisitedSpotsEntity;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import com.spotlightsarajevo.modules.media.utils.MediaUtilities;
import com.spotlightsarajevo.modules.spot.api.dto.*;
import com.spotlightsarajevo.modules.spot.domain.SpotDAO;
import com.spotlightsarajevo.modules.spot.domain.SpotTagsDAO;
import com.spotlightsarajevo.modules.spot.domain.SpotWorkHoursDAO;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotEntity;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotWorkHoursEntity;
import com.spotlightsarajevo.modules.spot.mapper.SpotMapper;
import com.spotlightsarajevo.modules.spot.mapper.SpotWorkHoursMapper;
import com.spotlightsarajevo.modules.spot.service.SpotServiceImpl;
import com.spotlightsarajevo.modules.spot.utils.SpotUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpotServiceUnitTests {
    @Mock
    private SpotDAO spotDAO;
    @Mock
    private  UserVisitedSpotsDAO userVisitedSpotsDAO;
    @Mock
    private  SpotTagsDAO spotTagsDAO;
    @Mock
    private  SpotWorkHoursDAO spotWorkHoursDAO;
    @Mock
    private  SpotMapper spotMapper;
    @Mock
    private  SpotWorkHoursMapper spotWorkHoursMapper;
    @Mock
    private  MediaUtilities mediaUtilities;
    @Mock
    private  SpotUtilities utils;
    @Mock
    private  CommonFunctions commonFunctions;

    @Mock
    private Principal principal;

    @InjectMocks
    private SpotServiceImpl spotService;

    /*==============================================*/
    /*================== FIND ALL  =================*/
    /*==============================================*/
    @Test
    void findAll_ShouldReturnPageOfSpotModels_WhenSpotsExist() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        SpotEntity entity = new SpotEntity();
        SpotModel model = new SpotModel();

        List<SpotEntity> entities = List.of(entity);
        List<SpotModel> models = List.of(model);

        Page<SpotEntity> entityPage = new PageImpl<>(entities, pageRequest, 1);

        when(spotDAO.findAll(pageRequest)).thenReturn(entityPage);
        when(spotMapper.entitiesToDtos(entities)).thenReturn(models);

        ResponseEntity<Page<SpotModel>> result = spotService.findAll(pageRequest);

        // Assert
        assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        Assertions.assertEquals(1, result.getBody().getTotalElements());
        Assertions.assertEquals(model, result.getBody().getContent().get(0));

        verify(spotDAO).findAll(pageRequest);
        verify(spotMapper).entitiesToDtos(entities);
    }

    /*==============================================*/
    /*============= FIND ALL PAGINATED =============*/
    /*==============================================*/

    @Test
    void findSpotsPaginated_ShouldReturnPaginatedSegmentModels_WhenSpotsExist() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        SpotEntity entity = new SpotEntity();
        SpotShorthandModel model = new SpotShorthandModel();

        List<Integer> categoryIds = new ArrayList<>();
        List<Integer> visitedSpotIds = new ArrayList<>();
        String searchTerm = new String();
        String sortOption = new String();
        BigDecimal userLatitude = new BigDecimal(1);
        BigDecimal userLongitude = new BigDecimal(1);
        Boolean excludeVisited = false;

        List<SpotEntity> entities = List.of(entity);
        List<SpotShorthandModel> models = List.of(model);

        Page<SpotEntity> entityPage = new PageImpl<>(entities, pageRequest, 1);

        when(spotDAO.findAll(any(Specification.class), eq(pageRequest))).thenReturn(entityPage);
        when(spotMapper.entitiesToShorthandDtos(entityPage.getContent())).thenReturn(models);

        ResponseEntity<Page<SpotShorthandModel>> result = spotService.findSpotsPaginated(
                pageRequest, searchTerm, sortOption, categoryIds, userLatitude, userLongitude, excludeVisited, principal);

        assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        Assertions.assertEquals(1, result.getBody().getTotalElements());
        Assertions.assertEquals(model, result.getBody().getContent().get(0));

        verify(spotDAO).findAll(any(Specification.class), eq(pageRequest));
        verify(spotMapper).entitiesToShorthandDtos(entities);
        verify(utils).setSpotCategoriesAndRating(entity);
        verify(utils).setSpotTags(entity);
        verify(mediaUtilities).lookupThumbnailImage(eq(entity), eq(ObjectType.SPOT), eq(entity.getId()));
    }

    @Test
    void findSpotsPaginated_ShouldThrowException_WhenPageRequestNull() {
        List<Integer> categoryIds = new ArrayList<>();
        String searchTerm = new String();
        String sortOption = new String();
        BigDecimal userLatitude = new BigDecimal(1);
        BigDecimal userLongitude = new BigDecimal(1);
        Boolean excludeVisited = false;

        assertThrows(SpotExceptions.SpotSystemException.class, () ->
                spotService.findSpotsPaginated(null, searchTerm, sortOption, categoryIds, userLatitude, userLongitude, excludeVisited, principal)
        );
    }

    @Test
    void findSpotsPaginated_ShouldExcludeVisitedSpots_WhenExcludeVisitedTrue() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        SpotEntity entity = new SpotEntity();
        UserEntity user = new UserEntity();
        Integer userID = 1;
        user.setId(userID);
        List<UserVisitedSpotsEntity> visitedSpots = new ArrayList<>();
        visitedSpots.add(new UserVisitedSpotsEntity());
        SpotShorthandModel model = new SpotShorthandModel();

        List<Integer> categoryIds = new ArrayList<>();
        String searchTerm = "";
        String sortOption = "ALPHABETICAL";
        BigDecimal userLatitude = new BigDecimal(1);
        BigDecimal userLongitude = new BigDecimal(1);
        Boolean excludeVisited = true;

        List<SpotEntity> entities = List.of(entity);
        List<SpotShorthandModel> models = List.of(model);

        Page<SpotEntity> entityPage = new PageImpl<>(entities, pageRequest, 1);

        when(commonFunctions.getUserFromPrincipal(principal)).thenReturn(user);
        when(userVisitedSpotsDAO.findAllByUserId(userID)).thenReturn(visitedSpots);
        when(spotDAO.findAll(any(Specification.class), eq(pageRequest))).thenReturn(entityPage);
        when(spotMapper.entitiesToShorthandDtos(entityPage.getContent())).thenReturn(models);

        ResponseEntity<Page<SpotShorthandModel>> result = spotService.findSpotsPaginated(
                pageRequest, searchTerm, sortOption, categoryIds, userLatitude, userLongitude, excludeVisited, principal);

        assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        Assertions.assertEquals(1, result.getBody().getTotalElements());

        verify(userVisitedSpotsDAO).findAllByUserId(userID);
        verify(spotDAO).findAll(any(Specification.class), eq(pageRequest));
        verify(spotMapper).entitiesToShorthandDtos(entities);
    }

    @Test
    void findSpotsPaginated_ShouldSortByProximity_WhenSortOptionProximity() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        SpotEntity entity = new SpotEntity();
        SpotShorthandModel model = new SpotShorthandModel();

        List<Integer> categoryIds = List.of(1, 2);
        String searchTerm = "restaurant";
        String sortOption = "PROXIMITY";
        BigDecimal userLatitude = new BigDecimal("43.8564");
        BigDecimal userLongitude = new BigDecimal("18.4131");
        Boolean excludeVisited = false;

        List<SpotEntity> entities = List.of(entity);
        List<SpotShorthandModel> models = List.of(model);

        Page<SpotEntity> entityPage = new PageImpl<>(entities, pageRequest, 1);

        when(spotDAO.findAll(any(Specification.class), eq(pageRequest))).thenReturn(entityPage);
        when(spotMapper.entitiesToShorthandDtos(entityPage.getContent())).thenReturn(models);

        ResponseEntity<Page<SpotShorthandModel>> result = spotService.findSpotsPaginated(
                pageRequest, searchTerm, sortOption, categoryIds, userLatitude, userLongitude, excludeVisited, principal);

        assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        Assertions.assertEquals(1, result.getBody().getTotalElements());
        Assertions.assertEquals(model, result.getBody().getContent().get(0));

        verify(spotDAO).findAll(any(Specification.class), eq(pageRequest));
        verify(spotMapper).entitiesToShorthandDtos(entities);
        verify(utils).setSpotCategoriesAndRating(entity);
        verify(utils).setSpotTags(entity);
        verify(mediaUtilities).lookupThumbnailImage(eq(entity), eq(ObjectType.SPOT), eq(entity.getId()));
    }

    /*==============================================*/
    /*============= FIND SPOT OVERVIEW =============*/
    /*==============================================*/

    @Test
    void findSpotOverview_ShouldReturnSpotOverview_WhenSlugIsValid() {
        String slug = "test-spot";
        SpotEntity entity = new SpotEntity();
        entity.setId(1);
        SpotOverviewModel model = new SpotOverviewModel();

        when(spotDAO.findBySlug(slug)).thenReturn(Optional.of(entity));
        when(spotMapper.entityOverviewToDto(entity)).thenReturn(model);

        ResponseEntity<SpotOverviewModel> result = spotService.findSpotOverview(slug);

        assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());

        verify(spotDAO).findBySlug(slug);
        verify(utils).setSpotCategoriesAndRating(entity);
        verify(utils).setSpotTags(entity);
        verify(utils).setSpotWorkHours(entity);
        verify(spotMapper).entityOverviewToDto(entity);
        verify(mediaUtilities).lookupThumbnailImage(eq(model), eq(ObjectType.SPOT), eq(entity.getId()));
        verify(mediaUtilities).lookupAllImages(eq(model), eq(ObjectType.SPOT), eq(entity.getId()));
    }

    @Test
    void findSpotOverview_ShouldThrowException_WhenSlugIsNull() {
        assertThrows(SpotExceptions.SpotNotFoundException.class, () ->
                spotService.findSpotOverview(null));
    }

    @Test
    void findSpotOverview_ShouldThrowException_WhenSlugIsBlank() {
        assertThrows(SpotExceptions.SpotNotFoundException.class, () ->
                spotService.findSpotOverview("   "));
    }

    @Test
    void findSpotOverview_ShouldThrowException_WhenSpotNotFound() {
        when(spotDAO.findBySlug("non-existent")).thenReturn(Optional.empty());

        assertThrows(SpotExceptions.SpotNotFoundException.class, () ->
                spotService.findSpotOverview("non-existent"));
    }

    /*====================================================*/
    /*============= FIND SPOT VISITED STATUS =============*/
    /*====================================================*/

    @Test
    void getSpotVisitedStatus_ShouldReturnTrue_WhenUserVisitedSpot() {
        Integer spotId = 1;
        SpotEntity spotEntity = new SpotEntity();
        spotEntity.setId(spotId);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);

        when(spotDAO.findById(spotId)).thenReturn(Optional.of(spotEntity));
        when(commonFunctions.getUserFromPrincipal(principal)).thenReturn(userEntity);
        when(utils.checkIfUserVisitedSpot(spotEntity.getId(), userEntity.getId())).thenReturn(true);

        ResponseEntity<Boolean> result = spotService.getSpotVisitedStatus(spotId, principal);

        assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody());

        verify(spotDAO).findById(spotId);
        verify(commonFunctions).getUserFromPrincipal(principal);
        verify(utils).checkIfUserVisitedSpot(spotEntity.getId(), userEntity.getId());
    }

    @Test
    void getSpotVisitedStatus_ShouldThrowException_WhenSpotIdIsNull() {
        assertThrows(SpotExceptions.SpotNotFoundException.class, () ->
                spotService.getSpotVisitedStatus(null, principal));
    }

    @Test
    void getSpotVisitedStatus_ShouldThrowException_WhenPrincipalIsNull() {
        assertThrows(EventExceptions.EventUnauthorizedException.class, () ->
                spotService.getSpotVisitedStatus(1, null));
    }

    @Test
    void getSpotVisitedStatus_ShouldThrowException_WhenSpotNotFound() {
        when(spotDAO.findById(1)).thenReturn(Optional.empty());
        when(commonFunctions.getUserFromPrincipal(principal)).thenReturn(new UserEntity());

        assertThrows(SpotExceptions.SpotNotFoundException.class, () ->
                spotService.getSpotVisitedStatus(1, principal));
    }

    /*==============================================*/
    /*============= CREATE SPOT METHOD =============*/
    /*==============================================*/

    @Test
    void create_ShouldCreateSpot_WhenDtoIsValid() {
        SpotCreateModel dto = new SpotCreateModel();
        dto.setTagIds(List.of(1, 2));
        dto.setWorkHoursModel(List.of(new SpotWorkHoursModel()));
        dto.setNewThumbnailImage(new MediaStoreCreateModel());
        dto.setToAddImages(List.of(new MediaStoreCreateModel()));

        SpotEntity entity = new SpotEntity();
        SpotEntity created = new SpotEntity();
        created.setId(1);
        SpotModel model = new SpotModel();

        SpotWorkHoursEntity workHoursEntity = new SpotWorkHoursEntity();

        when(spotMapper.dtoToEntity(dto)).thenReturn(entity);
        when(spotDAO.save(entity)).thenReturn(created);
        when(spotWorkHoursMapper.dtoToEntity(any(SpotWorkHoursModel.class))).thenReturn(workHoursEntity);
        when(spotMapper.entityToDto(created)).thenReturn(model);

        ResponseEntity<SpotModel> result = spotService.create(dto);

        assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(model, result.getBody());

        verify(spotMapper).dtoToEntity(dto);
        verify(spotDAO).save(entity);
        verify(spotTagsDAO).saveAllAndFlush(anyList());
        verify(spotWorkHoursDAO).saveAllAndFlush(anyList());
        verify(mediaUtilities).updateThumbnailImage(eq(dto.getNewThumbnailImage()), eq(created.getId()), eq(ObjectType.SPOT), eq("Admin User"));
        verify(mediaUtilities).addImages(eq(dto.getToAddImages()), eq(created.getId()), eq(ObjectType.SPOT), eq("Admin User"));
        verify(spotMapper).entityToDto(created);
    }

    @Test
    void create_ShouldThrowException_WhenDtoIsNull() {
        assertThrows(SpotExceptions.SpotSystemException.class, () ->
                spotService.create(null));
    }

    @Test
    void create_ShouldSkipTags_WhenTagIdsAreNull() {
        SpotCreateModel dto = new SpotCreateModel();
        dto.setTagIds(null);
        dto.setWorkHoursModel(null);

        SpotEntity entity = new SpotEntity();
        SpotEntity created = new SpotEntity();
        created.setId(1);

        when(spotMapper.dtoToEntity(dto)).thenReturn(entity);
        when(spotDAO.save(entity)).thenReturn(created);
        when(spotMapper.entityToDto(created)).thenReturn(new SpotModel());

        spotService.create(dto);

        verify(spotTagsDAO, never()).saveAllAndFlush(anyList());
        verify(spotWorkHoursDAO, never()).saveAllAndFlush(anyList());
    }

    /*==============================================*/
    /*============= DELETE SPOT METHOD =============*/
    /*==============================================*/

    @Test
    void delete_ShouldFindAndDelete_WhenIDPresent() {
        SpotEntity baseEntity = new SpotEntity();
        Optional<SpotEntity> entity = Optional.of(baseEntity);
        Integer id = 1;
        SpotModel model = new SpotModel();

        when(spotDAO.findById(id)).thenReturn(entity);
        when(spotMapper.entityToDto(entity.get())).thenReturn(model);

        ResponseEntity<SpotModel> result = spotService.delete(id);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(model, result.getBody());

        verify(spotDAO).findById(id);
        verify(spotDAO).delete(entity.get());
        verify(spotMapper).entityToDto(entity.get());
    }

    @Test
    void delete_ShouldThrowException_WhenIDNotPresent() {
        Integer id = null;
        when(spotDAO.findById(1)).thenReturn(Optional.empty());

        assertThrows(SpotExceptions.SpotNotFoundException.class, () -> {
            spotService.delete(1);
        });
    }
}

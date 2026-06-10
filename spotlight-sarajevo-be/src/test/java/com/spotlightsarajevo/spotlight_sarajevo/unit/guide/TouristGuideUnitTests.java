package com.spotlightsarajevo.spotlight_sarajevo.unit.guide;

import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.exceptions.TouristGuideExceptions;
import com.spotlightsarajevo.common.utils.CommonFunctions;
import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.guide.api.dto.*;
import com.spotlightsarajevo.modules.guide.domain.TouristGuideDAO;
import com.spotlightsarajevo.modules.guide.domain.TouristGuideSectionDAO;
import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideEntity;
import com.spotlightsarajevo.modules.guide.domain.entity.TouristGuideSectionEntity;
import com.spotlightsarajevo.modules.guide.mapper.TouristGuideMapper;
import com.spotlightsarajevo.modules.guide.service.TouristGuideServiceImpl;
import com.spotlightsarajevo.modules.guide.utils.TouristGuideUtilities;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreCreateModel;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import com.spotlightsarajevo.modules.media.utils.MediaUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TouristGuideUnitTests {
    @Mock
    TouristGuideDAO touristGuideDAO;
    @Mock
    TouristGuideSectionDAO touristGuideSectionDAO;
    @Mock
    TouristGuideMapper touristGuideMapper;
    @Mock
    TouristGuideUtilities guideUtilities;
    @Mock
    MediaUtilities mediaUtilities;
    @Mock
    CommonFunctions utils;
    @Mock
    Principal principal;

    @InjectMocks
    TouristGuideServiceImpl guideService;

    UserEntity userEntity;

    @BeforeEach
    void init() {
        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setIsAdmin(true);
        when(utils.getUserFromPrincipal(principal)).thenReturn(userEntity);
    }

    @Test
    void findAllGuides_ShouldReturnAllGuides_WhenNoError() {
        // Arrange
        TouristGuideEntity entity = new TouristGuideEntity();
        entity.setId(1);
        List<TouristGuideEntity> entities = List.of(entity);
        TouristGuideShorthandModel shorthandModel = new TouristGuideShorthandModel();
        List<TouristGuideShorthandModel> shorthandModels = List.of(shorthandModel);

        when(touristGuideDAO.findAll()).thenReturn(entities);
        when(touristGuideMapper.entitiesToShorthandDtos(entities)).thenReturn(shorthandModels);

        // Act
        ResponseEntity<List<TouristGuideShorthandModel>> result = guideService.findAllGuides();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(shorthandModel, result.getBody().get(0));

        verify(touristGuideDAO).findAll();
        verify(guideUtilities).setGuideCategoryNames(entity);
        verify(mediaUtilities).lookupThumbnailImage(entity, ObjectType.GUIDE_THUMBNAIL, entity.getId());
        verify(touristGuideMapper).entitiesToShorthandDtos(entities);
    }

    @Test
    void findGuidesPaginated_ShouldReturnGuides_WhenNoError() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        String searchTerm = "test";
        String sortOption = "name";
        TouristGuideEntity entity = new TouristGuideEntity();
        entity.setId(1);
        List<TouristGuideEntity> entityList = List.of(entity);
        Page<TouristGuideEntity> entityPage = new PageImpl<>(entityList, pageRequest, 1);
        TouristGuideShorthandModel shorthandModel = new TouristGuideShorthandModel();
        List<TouristGuideShorthandModel> shorthandModels = List.of(shorthandModel);

        when(touristGuideDAO.findAll(any(Specification.class), eq(pageRequest))).thenReturn(entityPage);
        when(touristGuideMapper.entitiesToShorthandDtos(entityList)).thenReturn(shorthandModels);

        // Act
        ResponseEntity<Page<TouristGuideShorthandModel>> result = guideService.findGuidesPaginated(pageRequest, searchTerm, sortOption);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().getContent().size());
        assertEquals(shorthandModel, result.getBody().getContent().get(0));
        assertEquals(1, result.getBody().getTotalElements());

        verify(touristGuideDAO).findAll(any(Specification.class), eq(pageRequest));
        verify(guideUtilities).setGuideCategoryNames(entity);
        verify(mediaUtilities).lookupThumbnailImage(entity, ObjectType.GUIDE_THUMBNAIL, entity.getId());
        verify(touristGuideMapper).entitiesToShorthandDtos(entityList);
    }

    @Test
    void findGuidesPaginated_ShouldThrowError_WhenPageRequestNull() {
        // Arrange
        PageRequest pageRequest = null;

        // Act & Assert
        assertThrows(TouristGuideExceptions.TouristGuideRequestException.class, () -> {
            guideService.findGuidesPaginated(pageRequest, "", "");
        });
    }

    @Test
    void findByCategory_ShouldReturnGuidesByCategory_WhenCategoryIdNotNull() {
        // Arrange
        Integer categoryId = 1;
        TouristGuideEntity entity = new TouristGuideEntity();
        entity.setId(1);
        List<TouristGuideEntity> entities = List.of(entity);
        TouristGuideShorthandModel shorthandModel = new TouristGuideShorthandModel();
        List<TouristGuideShorthandModel> shorthandModels = List.of(shorthandModel);

        when(touristGuideDAO.findByCategoryId(categoryId)).thenReturn(entities);
        when(touristGuideMapper.entitiesToShorthandDtos(entities)).thenReturn(shorthandModels);

        // Act
        ResponseEntity<List<TouristGuideShorthandModel>> result = guideService.findByCategory(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(shorthandModel, result.getBody().get(0));

        verify(touristGuideDAO).findByCategoryId(categoryId);
        verify(guideUtilities).setGuideCategoryNames(entity);
        verify(mediaUtilities).lookupThumbnailImage(entity, ObjectType.GUIDE_THUMBNAIL, entity.getId());
        verify(touristGuideMapper).entitiesToShorthandDtos(entities);
    }

    @Test
    void findByCategory_ShouldThrowError_WhenCategoryIdNull() {
        // Arrange
        Integer categoryId = null;

        // Act & Assert
        assertThrows(TouristGuideExceptions.TouristGuideRequestException.class, () -> {
            guideService.findByCategory(categoryId);
        });
    }

    @Test
    void findCategoryOverview_ShouldReturnOverview_WhenSlugNotNull() {
        // Arrange
        String slug = "example";
        TouristGuideEntity entity = new TouristGuideEntity();
        entity.setId(1);
        Optional<TouristGuideEntity> optionalEntity = Optional.of(entity);
        TouristGuideOverviewModel overviewModel = new TouristGuideOverviewModel();
        List<TouristGuideSectionEntity> sectionEntities = new ArrayList<>();
        TouristGuideSectionEntity sectionEntity = new TouristGuideSectionEntity();
        sectionEntity.setId(1);
        sectionEntities.add(sectionEntity);
        List<TouristGuideSectionModel> sectionModels = new ArrayList<>();
        TouristGuideSectionModel sectionModel = new TouristGuideSectionModel();
        sectionModels.add(sectionModel);

        when(touristGuideDAO.findBySlug(slug)).thenReturn(optionalEntity);
        when(touristGuideMapper.entityToOverviewDto(entity)).thenReturn(overviewModel);
        when(touristGuideSectionDAO.findAllByGuideId(entity.getId())).thenReturn(sectionEntities);
        when(touristGuideMapper.sectionEntityToDto(sectionEntity)).thenReturn(sectionModel);

        // Act
        ResponseEntity<TouristGuideOverviewModel> result = guideService.findGuideOverview(slug);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(overviewModel, result.getBody());
        assertNotNull(result.getBody().getSections());
        assertEquals(1, result.getBody().getSections().size());

        verify(touristGuideDAO).findBySlug(slug);
        verify(mediaUtilities).lookupThumbnailImage(entity, ObjectType.GUIDE_THUMBNAIL, entity.getId());
        verify(guideUtilities).setGuideCategoryNames(entity);
        verify(touristGuideMapper).entityToOverviewDto(entity);
        verify(touristGuideSectionDAO).findAllByGuideId(entity.getId());
        verify(mediaUtilities).lookupThumbnailImage(sectionEntity, ObjectType.SECTION_GUIDE, sectionEntity.getId());
        verify(touristGuideMapper).sectionEntityToDto(sectionEntity);
    }

    @Test
    void findCategoryOverview_ShouldThrowError_WhenSlugNull() {
        // Arrange
        String slug = null;

        // Act & Assert
        assertThrows(TouristGuideExceptions.TouristGuideRequestException.class, () -> {
            guideService.findGuideOverview(slug);
        });
    }

    @Test
    void create_ShouldCreateNewGuide_WhenRequestCorrect() {
        // Arrange
        TouristGuideCreateModel request = new TouristGuideCreateModel();
        request.setGuideTitleEn("Test Guide");
        request.setThumbnailImage(new MediaStoreModel());
        request.setSections(new ArrayList<>());

        TouristGuideEntity entity = new TouristGuideEntity();
        entity.setId(1);
        entity.setSlug("test-guide");
        TouristGuideOverviewModel overviewModel = new TouristGuideOverviewModel();

        when(touristGuideDAO.findBySlug(anyString())).thenReturn(Optional.empty());
        when(touristGuideMapper.dtoToEntity(request)).thenReturn(entity);
        when(touristGuideDAO.save(entity)).thenReturn(entity);
        when(touristGuideMapper.entityToOverviewDto(entity)).thenReturn(overviewModel);

        // Act
        ResponseEntity<TouristGuideOverviewModel> result = guideService.create(request, principal);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());

        verify(touristGuideMapper).dtoToEntity(request);
        verify(touristGuideDAO).save(entity);
        verify(mediaUtilities).create(any(MediaStoreCreateModel.class));
        verify(touristGuideMapper).entityToOverviewDto(entity);
    }

    @Test
    void create_ShouldThrowException_WhenRequestNull() {
        // Arrange
        TouristGuideCreateModel request = null;

        // Act & Assert
        assertThrows(TouristGuideExceptions.TouristGuideRequestException.class, () -> {
            guideService.create(request, principal);
        });
    }

    @Test
    void create_ShouldThrowException_WhenUserNotAdmin() {
        // Arrange
        userEntity.setIsAdmin(false);
        TouristGuideCreateModel request = new TouristGuideCreateModel();

        // Act & Assert
        assertThrows(TouristGuideExceptions.TouristGuideAuth.class, () -> {
            guideService.create(request, principal);
        });
    }

    @Test
    void update_ShouldUpdateGuide_WhenRequestCorrect() {
        TouristGuideUpdateModel request = new TouristGuideUpdateModel();
        request.setToAddSections(new ArrayList<>());
        request.setToUpdateSections(new ArrayList<>());
        request.setToDeleteSections(new ArrayList<>());
        Optional<TouristGuideEntity> entity = Optional.of(new TouristGuideEntity());
        TouristGuideModel model = new TouristGuideModel();

        when(touristGuideDAO.findById(request.getId())).thenReturn(entity);
        when(touristGuideMapper.entityToDto(entity.get())).thenReturn(model);

        ResponseEntity<TouristGuideModel> result = guideService.update(
                request, principal
        );

        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

        verify(touristGuideMapper).updateEntityFromDto(request, entity.get());
        verify(touristGuideDAO).save(entity.get());
        verify(mediaUtilities).updateThumbnailImage(request.getNewThumbnailImage(), entity.get().getId(), ObjectType.GUIDE_THUMBNAIL, userEntity.getUsername());
        verify(guideUtilities, never()).createNewSections(any(), any());
        verify(guideUtilities, never()).updateExistingSections(any(), any());
        verify(guideUtilities, never()).deleteExistingSections(any(), any());

    }

    @Test
    void update_ShouldThrowError_WhenUserNotAdmin() {
        // Arrange
        userEntity.setIsAdmin(false);
        TouristGuideUpdateModel request = new TouristGuideUpdateModel();

        // Act & Assert
        assertThrows(TouristGuideExceptions.TouristGuideAuth.class, () -> {
            guideService.update(request, principal);
        });
    }

    @Test
    void delete_ShouldDeleteSpot_WhenIDNotNull(){
        Integer id = 1;

        TouristGuideEntity entity = new TouristGuideEntity();
        Optional<TouristGuideEntity> entityOptional = Optional.of(entity);
        TouristGuideModel model = new TouristGuideModel();

        when(touristGuideDAO.findById(id)).thenReturn(entityOptional);
        when(touristGuideMapper.entityToDto(entityOptional.get())).thenReturn(model);

        ResponseEntity<TouristGuideModel> result = guideService.delete(id);

        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

        verify(touristGuideDAO).findById(id);
        verify(touristGuideDAO).delete(entity);
        verify(touristGuideMapper).entityToDto(entityOptional.get());
    }

    @Test
    void delete_ShouldThrowError_WhenIdNull(){
        Integer id = null;

        assertThrows(TouristGuideExceptions.TouristGuideNotFound.class, () -> {
           guideService.delete(id);
        });
    }

}

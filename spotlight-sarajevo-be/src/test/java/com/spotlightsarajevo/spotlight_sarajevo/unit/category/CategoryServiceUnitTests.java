package com.spotlightsarajevo.spotlight_sarajevo.unit.category;

import com.spotlightsarajevo.modules.category.api.dto.EventCategoryModel;
import com.spotlightsarajevo.modules.category.api.dto.SpotCategoryModel;
import com.spotlightsarajevo.modules.category.api.dto.TouristGuideCategoryModel;
import com.spotlightsarajevo.modules.category.domain.EventCategoryDAO;
import com.spotlightsarajevo.modules.category.domain.SpotCategoryDAO;
import com.spotlightsarajevo.modules.category.domain.entity.EventCategoryEntity;
import com.spotlightsarajevo.modules.category.domain.entity.SpotCategoryEntity;
import com.spotlightsarajevo.modules.category.domain.entity.TouristGuideCategoryEntity;
import com.spotlightsarajevo.modules.category.mapper.EventCategoryMapper;
import com.spotlightsarajevo.modules.category.mapper.SpotCategoryMapper;
import com.spotlightsarajevo.modules.category.mapper.TouristGuideCategoryMapper;
import com.spotlightsarajevo.modules.category.service.CategoryServiceImpl;
import com.spotlightsarajevo.modules.guide.domain.TouristGuideCategoryDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CategoryServiceUnitTests {
    @Mock
    SpotCategoryDAO spotCategoryDAO;
    @Mock
    EventCategoryDAO eventCategoryDAO;
    @Mock
    TouristGuideCategoryDAO touristGuideCategoryDAO;
    @Mock
    SpotCategoryMapper spotCategoryMapper;
    @Mock
    EventCategoryMapper eventCategoryMapper;
    @Mock
    TouristGuideCategoryMapper touristGuideCategoryMapper;
    @InjectMocks
    CategoryServiceImpl categoryService;

    @Test
    void findAllSpotCategories_ShouldReturnAllSpotCategories_WhenNoError() {
        // Arrange
        SpotCategoryEntity entity = new SpotCategoryEntity();
        entity.setId(1);
        entity.setSpotCategoryNameEn("Historical Site");
        List<SpotCategoryEntity> entities = new ArrayList<>();
        entities.add(entity);

        SpotCategoryModel model = new SpotCategoryModel();
        model.setId(1);
        model.setSpotCategoryNameEn("Historical Site");
        List<SpotCategoryModel> models = new ArrayList<>();
        models.add(model);

        when(spotCategoryDAO.findAll()).thenReturn(entities);
        when(spotCategoryMapper.entitiesToDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<SpotCategoryModel>> result = categoryService.findAllSpotCategories();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(model, result.getBody().get(0));

        verify(spotCategoryDAO).findAll();
        verify(spotCategoryMapper).entitiesToDtos(entities);
    }

    @Test
    void findAllSpotCategories_ShouldReturnEmptyList_WhenNoSpotCategoriesExist() {
        // Arrange
        List<SpotCategoryEntity> entities = new ArrayList<>();
        List<SpotCategoryModel> models = new ArrayList<>();

        when(spotCategoryDAO.findAll()).thenReturn(entities);
        when(spotCategoryMapper.entitiesToDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<SpotCategoryModel>> result = categoryService.findAllSpotCategories();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());

        verify(spotCategoryDAO).findAll();
        verify(spotCategoryMapper).entitiesToDtos(entities);
    }

    @Test
    void findAllEventCategories_ShouldReturnAllEventCategories_WhenNoError() {
        // Arrange
        EventCategoryEntity entity = new EventCategoryEntity();
        entity.setId(1);
        entity.setEventCategoryNameEn("Festival");
        List<EventCategoryEntity> entities = new ArrayList<>();
        entities.add(entity);

        EventCategoryModel model = new EventCategoryModel();
        model.setId(1);
        model.setEventCategoryNameEn("Festival");
        List<EventCategoryModel> models = new ArrayList<>();
        models.add(model);

        when(eventCategoryDAO.findAll()).thenReturn(entities);
        when(eventCategoryMapper.entitiesToDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<EventCategoryModel>> result = categoryService.findAllEventCategories();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(model, result.getBody().get(0));

        verify(eventCategoryDAO).findAll();
        verify(eventCategoryMapper).entitiesToDtos(entities);
    }

    @Test
    void findAllEventCategories_ShouldReturnEmptyList_WhenNoEventCategoriesExist() {
        // Arrange
        List<EventCategoryEntity> entities = new ArrayList<>();
        List<EventCategoryModel> models = new ArrayList<>();

        when(eventCategoryDAO.findAll()).thenReturn(entities);
        when(eventCategoryMapper.entitiesToDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<EventCategoryModel>> result = categoryService.findAllEventCategories();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());

        verify(eventCategoryDAO).findAll();
        verify(eventCategoryMapper).entitiesToDtos(entities);
    }

    @Test
    void findAllGuideCategories_ShouldReturnAllGuideCategories_WhenNoError() {
        // Arrange
        TouristGuideCategoryEntity entity = new TouristGuideCategoryEntity();
        entity.setId(Long.valueOf(1));
        entity.setCategoryNameEn("Architecture");
        List<TouristGuideCategoryEntity> entities = new ArrayList<>();
        entities.add(entity);

        TouristGuideCategoryModel model = new TouristGuideCategoryModel();
        model.setId(Long.valueOf(1));
        model.setCategoryNameEn("Architecture");
        List<TouristGuideCategoryModel> models = new ArrayList<>();
        models.add(model);

        when(touristGuideCategoryDAO.findAll()).thenReturn(entities);
        when(touristGuideCategoryMapper.entitiesToDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<TouristGuideCategoryModel>> result = categoryService.findAllGuideCategories();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(model, result.getBody().get(0));

        verify(touristGuideCategoryDAO).findAll();
        verify(touristGuideCategoryMapper).entitiesToDtos(entities);
    }

    @Test
    void findAllGuideCategories_ShouldReturnEmptyList_WhenNoGuideCategoriesExist() {
        // Arrange
        List<TouristGuideCategoryEntity> entities = new ArrayList<>();
        List<TouristGuideCategoryModel> models = new ArrayList<>();

        when(touristGuideCategoryDAO.findAll()).thenReturn(entities);
        when(touristGuideCategoryMapper.entitiesToDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<TouristGuideCategoryModel>> result = categoryService.findAllGuideCategories();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());

        verify(touristGuideCategoryDAO).findAll();
        verify(touristGuideCategoryMapper).entitiesToDtos(entities);
    }
}

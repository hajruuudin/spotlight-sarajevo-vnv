package com.spotlightsarajevo.spotlight_sarajevo.unit;

import com.spotlightsarajevo.modules.tag.api.dto.TagModel;
import com.spotlightsarajevo.modules.tag.domain.TagDAO;
import com.spotlightsarajevo.modules.tag.domain.entity.TagEntity;
import com.spotlightsarajevo.modules.tag.mappers.TagMapper;
import com.spotlightsarajevo.modules.tag.service.TagServiceImpl;
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
public class TagServiceUnitTests {
    @Mock
    TagDAO tagDAO;
    @Mock
    TagMapper tagMapper;
    @InjectMocks
    TagServiceImpl tagService;

    @Test
    void findAll_ShouldReturnAllTags_WhenNoError() {
        // Arrange
        TagEntity entity = new TagEntity();
        entity.setId(1);
        entity.setTagNameEn("Nature");
        List<TagEntity> entities = new ArrayList<>();
        entities.add(entity);

        TagModel model = new TagModel();
        model.setId(1);
        model.setTagNameEn("Nature");
        List<TagModel> models = new ArrayList<>();
        models.add(model);

        when(tagDAO.findAll()).thenReturn(entities);
        when(tagMapper.entitiesToDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<TagModel>> result = tagService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(model, result.getBody().get(0));

        verify(tagDAO).findAll();
        verify(tagMapper).entitiesToDtos(entities);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTagsExist() {
        // Arrange
        List<TagEntity> entities = new ArrayList<>();
        List<TagModel> models = new ArrayList<>();

        when(tagDAO.findAll()).thenReturn(entities);
        when(tagMapper.entitiesToDtos(entities)).thenReturn(models);

        // Act
        ResponseEntity<List<TagModel>> result = tagService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());

        verify(tagDAO).findAll();
        verify(tagMapper).entitiesToDtos(entities);
    }
}

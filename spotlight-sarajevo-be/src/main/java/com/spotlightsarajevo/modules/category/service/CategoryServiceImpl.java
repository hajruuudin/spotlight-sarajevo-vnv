package com.spotlightsarajevo.modules.category.service;

import com.spotlightsarajevo.modules.category.api.dto.TouristGuideCategoryModel;
import com.spotlightsarajevo.modules.category.domain.EventCategoryDAO;
import com.spotlightsarajevo.modules.category.domain.SpotCategoryDAO;
import com.spotlightsarajevo.modules.category.api.dto.EventCategoryModel;
import com.spotlightsarajevo.modules.category.api.dto.SpotCategoryModel;
import com.spotlightsarajevo.modules.category.domain.entity.EventCategoryEntity;
import com.spotlightsarajevo.modules.category.domain.entity.SpotCategoryEntity;
import com.spotlightsarajevo.modules.category.domain.entity.TouristGuideCategoryEntity;
import com.spotlightsarajevo.modules.category.mapper.EventCategoryMapper;
import com.spotlightsarajevo.modules.category.mapper.SpotCategoryMapper;
import com.spotlightsarajevo.modules.category.mapper.TouristGuideCategoryMapper;
import com.spotlightsarajevo.modules.guide.domain.TouristGuideCategoryDAO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    SpotCategoryDAO spotCategoryDAO;
    EventCategoryDAO eventCategoryDAO;
    TouristGuideCategoryDAO touristGuideCategoryDAO;
    SpotCategoryMapper spotCategoryMapper;
    EventCategoryMapper eventCategoryMapper;
    TouristGuideCategoryMapper touristGuideCategoryMapper;


    @Override
    public ResponseEntity<List<SpotCategoryModel>> findAllSpotCategories() {
        try{
            List<SpotCategoryEntity> entities = spotCategoryDAO.findAll();
            List<SpotCategoryModel> models = spotCategoryMapper.entitiesToDtos(entities);

            return ResponseEntity.status(200).body(models);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseEntity<List<EventCategoryModel>> findAllEventCategories() {
        try{
            List<EventCategoryEntity> entities = eventCategoryDAO.findAll();
            List<EventCategoryModel> models = eventCategoryMapper.entitiesToDtos(entities);

            return ResponseEntity.status(200).body(models);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public ResponseEntity<List<TouristGuideCategoryModel>> findAllGuideCategories() {
        try{
            List<TouristGuideCategoryEntity> entities = touristGuideCategoryDAO.findAll();
            List<TouristGuideCategoryModel> models = touristGuideCategoryMapper.entitiesToDtos(entities);

            return ResponseEntity.status(200).body(models);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}

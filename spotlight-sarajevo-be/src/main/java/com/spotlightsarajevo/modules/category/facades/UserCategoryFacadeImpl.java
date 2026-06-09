package com.spotlightsarajevo.modules.category.facades;

import com.spotlightsarajevo.modules.category.api.dto.EventCategoryModel;
import com.spotlightsarajevo.modules.category.api.dto.SpotCategoryModel;
import com.spotlightsarajevo.modules.category.domain.EventCategoryDAO;
import com.spotlightsarajevo.modules.category.domain.SpotCategoryDAO;
import com.spotlightsarajevo.modules.category.mapper.EventCategoryMapper;
import com.spotlightsarajevo.modules.category.mapper.SpotCategoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserCategoryFacadeImpl implements UserCategoryFacade {
    SpotCategoryDAO spotCategoryDAO;
    EventCategoryDAO eventCategoryDAO;
    SpotCategoryMapper spotCategoryMapper;
    EventCategoryMapper eventCategoryMapper;

    @Override
    public List<SpotCategoryModel> getUserFavoriteSpotCategories(List<Integer> categoryIds) {
        if(categoryIds == null || categoryIds.isEmpty()){
            return List.of();
        }

        List<SpotCategoryModel> models = new ArrayList<>();

        for(Integer id : categoryIds){
            spotCategoryDAO.findById(id).ifPresent(category -> {
                SpotCategoryModel model = spotCategoryMapper.entityToDto(category);
                models.add(model);
            });
        }

        return models;
    }

    @Override
    public List<EventCategoryModel> getUserFavoriteEventCategories(List<Integer> categoryIds) {
        if(categoryIds == null || categoryIds.isEmpty()){
            return List.of();
        }

        List<EventCategoryModel> models = new ArrayList<>();

        for(Integer id : categoryIds){
            eventCategoryDAO.findById(id).ifPresent(category -> {
                EventCategoryModel model = eventCategoryMapper.entityToDto(category);
                models.add(model);
            });
        }

        return models;
    }
}

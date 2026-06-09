package com.spotlightsarajevo.modules.category.facades;

import com.spotlightsarajevo.modules.category.api.dto.EventCategoryModel;
import com.spotlightsarajevo.modules.category.api.dto.SpotCategoryModel;

import java.util.List;

public interface UserCategoryFacade {
    List<SpotCategoryModel> getUserFavoriteSpotCategories(List<Integer> categoryIds);
    List<EventCategoryModel> getUserFavoriteEventCategories(List<Integer> categoryIds);
}

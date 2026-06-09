package com.spotlightsarajevo.modules.auth.utils;

import com.spotlightsarajevo.modules.auth.domain.entity.UserEntity;
import com.spotlightsarajevo.modules.category.facades.UserCategoryFacade;
import com.spotlightsarajevo.modules.event.facades.EventExistsFacade;
import com.spotlightsarajevo.modules.spot.facades.SpotExistsFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserInfoUtils {
    private UserCategoryFacade userCategoryFacade;
    private SpotExistsFacade spotExistsFacade;
    private EventExistsFacade eventExistsFacade;

    public void setFavoriteCategories(UserEntity entity, List<Integer> spotIds, List<Integer> eventIds){
        entity.setFavoriteSpotCategories(
                userCategoryFacade.getUserFavoriteSpotCategories(
                       spotIds
                )
        );

        entity.setFavoriteEventCategories(
                userCategoryFacade.getUserFavoriteEventCategories(
                        eventIds
                )
        );
    }

    public boolean validateSpotExists(Integer spotId){
        return spotExistsFacade.spotExists(spotId);
    }

    public boolean validateEventExists(Integer eventId){
        return eventExistsFacade.eventExists(eventId);
    }
}

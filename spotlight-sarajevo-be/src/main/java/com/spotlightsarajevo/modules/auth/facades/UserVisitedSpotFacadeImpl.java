package com.spotlightsarajevo.modules.auth.facades;

import com.spotlightsarajevo.modules.auth.domain.UserVisitedSpotsDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserVisitedSpotFacadeImpl implements UserVisitedSpotFacade {
    UserVisitedSpotsDAO userVisitedSpotsDAO;

    @Override
    public boolean checkIfUserVisitedSpot(Integer userId, Integer spotId) {
        return userVisitedSpotsDAO.existsByUserIdAndSpotId(userId, spotId);
    }
}

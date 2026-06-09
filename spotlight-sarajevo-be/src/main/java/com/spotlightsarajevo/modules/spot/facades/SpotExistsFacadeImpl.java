package com.spotlightsarajevo.modules.spot.facades;

import com.spotlightsarajevo.modules.spot.domain.SpotDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SpotExistsFacadeImpl implements SpotExistsFacade {
    SpotDAO spotDAO;

    @Override
    public boolean spotExists(Integer spotId) {
        return spotDAO.existsById(spotId);
    }
}

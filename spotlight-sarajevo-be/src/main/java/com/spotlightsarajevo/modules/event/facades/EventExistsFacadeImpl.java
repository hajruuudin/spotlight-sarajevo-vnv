package com.spotlightsarajevo.modules.event.facades;

import com.spotlightsarajevo.modules.event.domain.EventDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventExistsFacadeImpl implements EventExistsFacade {
    private final EventDAO eventDAO;

    @Override
    public boolean eventExists(Integer eventId) {
        return eventDAO.existsById(eventId);
    }
}

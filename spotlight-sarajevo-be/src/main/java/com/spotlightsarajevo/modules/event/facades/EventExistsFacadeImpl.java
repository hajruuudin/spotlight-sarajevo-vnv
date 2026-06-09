package com.spotlightsarajevo.modules.event.facades;

import com.spotlightsarajevo.modules.event.domain.EventDAO;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;
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

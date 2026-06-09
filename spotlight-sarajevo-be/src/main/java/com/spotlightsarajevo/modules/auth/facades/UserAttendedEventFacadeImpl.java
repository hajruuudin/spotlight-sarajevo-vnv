package com.spotlightsarajevo.modules.auth.facades;

import com.spotlightsarajevo.modules.auth.domain.UserAttendedEventsDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAttendedEventFacadeImpl implements UserAttendedEventFacade {
    UserAttendedEventsDAO userAttendedEventsDAO;

    @Override
    public boolean checkIfUserAttendedEvent(Integer userId, Integer eventId) {
        return userAttendedEventsDAO.existsByUserIdAndEventId(userId, eventId);
    }
}

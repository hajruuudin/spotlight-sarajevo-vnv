package com.spotlightsarajevo.modules.event.facades;

import com.spotlightsarajevo.modules.event.api.dto.EventShorthandModel;

public interface EventCollectionFacade {
    EventShorthandModel getEventShorthand(Integer eventId);
}

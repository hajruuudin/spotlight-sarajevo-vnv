package com.spotlightsarajevo.modules.event.facades;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.exceptions.EventExceptions;
import com.spotlightsarajevo.modules.event.api.dto.EventShorthandModel;
import com.spotlightsarajevo.modules.event.domain.EventDAO;
import com.spotlightsarajevo.modules.event.domain.entity.EventEntity;
import com.spotlightsarajevo.modules.event.mapper.EventMapper;
import com.spotlightsarajevo.modules.event.utils.EventUtilities;
import com.spotlightsarajevo.modules.media.utils.MediaUtilities;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventCollectionFacadeImpl implements EventCollectionFacade {
    private final EventDAO eventDAO;
    private final EventMapper eventMapper;
    private final EventUtilities eventUtilities;
    private final MediaUtilities mediaUtilities;

    @Override
    public EventShorthandModel getEventShorthand(Integer eventId) {
        EventEntity event = eventDAO.findById(eventId)
                .orElseThrow(() -> new EventExceptions.EventNotFoundException(
                        ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND
                ));

        eventUtilities.setEventTags(event);
        eventUtilities.setEventCategories(event);
        EventShorthandModel dto = eventMapper.entityToShorthandDto(event);

        mediaUtilities.lookupThumbnailImage(dto, ObjectType.EVENT, dto.getId());
        return dto;
    }
}

package com.spotlightsarajevo.modules.event.utils;

import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.modules.auth.facades.UserAttendedEventFacade;
import com.spotlightsarajevo.modules.category.domain.EventCategoryDAO;
import com.spotlightsarajevo.modules.category.domain.entity.EventCategoryEntity;
import com.spotlightsarajevo.modules.event.api.dto.EventOrganiserModel;
import com.spotlightsarajevo.modules.event.api.dto.EventUpdateModel;
import com.spotlightsarajevo.modules.event.domain.EventOrganiserDAO;
import com.spotlightsarajevo.modules.event.domain.EventOrganiserReviewStatsDAO;
import com.spotlightsarajevo.modules.event.domain.EventTagsDAO;
import com.spotlightsarajevo.modules.event.domain.entity.EventEntity;
import com.spotlightsarajevo.modules.event.domain.entity.EventOrganiserEntity;
import com.spotlightsarajevo.modules.event.domain.entity.EventOrganiserReviewStatsEntity;
import com.spotlightsarajevo.modules.event.domain.entity.EventTagsEntity;
import com.spotlightsarajevo.modules.event.mapper.EventOrganiserMapper;
import com.spotlightsarajevo.modules.media.utils.MediaUtilities;
import com.spotlightsarajevo.modules.tag.domain.TagDAO;
import com.spotlightsarajevo.modules.tag.domain.entity.TagEntity;
import com.spotlightsarajevo.modules.tag.mappers.TagMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class EventUtilities {
    EventCategoryDAO eventCategoryDAO;
    EventTagsDAO eventTagsDAO;
    EventOrganiserDAO eventOrganiserDAO;
    EventOrganiserReviewStatsDAO eventOrganiserReviewStatsDAO;
    UserAttendedEventFacade userAttendedEventFacade;
    TagDAO tagDAO;
    TagMapper tagMapper;
    EventOrganiserMapper eventOrganiserMapper;
    MediaUtilities mediaUtilities;

    public void setEventCategories(EventEntity entity){
        Optional<EventCategoryEntity> category = eventCategoryDAO.findById(entity.getCategoryId());

        if (category.isPresent()) {
            entity.setCategoryNameBs(category.get().getEventCategoryNameBs());
            entity.setCategoryNameEn(category.get().getEventCategoryNameEn());
        }
    }

    public void setEventTags(EventEntity entity){
        List<TagEntity> tagEntities = new ArrayList<>();
        List<EventTagsEntity> eventTags = eventTagsDAO.findByEventId(entity.getId());

        for(EventTagsEntity eventTag : eventTags){
            Optional<TagEntity> tag = tagDAO.findById(eventTag.getTagId());

            if(tag.isPresent()){
                tagEntities.add(tag.get());
            } else {
                // Do some logging or something
            }
        }

        entity.setEventTags(tagMapper.entitiesToDtos(tagEntities));
    }

    public void setEventOrganiserAndStats(EventEntity entity){
        Optional<EventOrganiserEntity> organiserEntity = eventOrganiserDAO.findById(entity.getOrganiserId());

        if(organiserEntity.isPresent()){
            Optional<EventCategoryEntity> category = eventCategoryDAO.findById(organiserEntity.get().getOrganiserCategoryId());
            EventOrganiserModel organiserModel = eventOrganiserMapper.entityToDto(organiserEntity.get());

            if (category.isPresent()) {
                organiserModel.setOrganiserCategoryNameBs(category.get().getEventCategoryNameBs());
                organiserModel.setOrganiserCategoryNameEn(category.get().getEventCategoryNameEn());
            }

            Optional<EventOrganiserReviewStatsEntity> organiserReviewStatsEntity = eventOrganiserReviewStatsDAO.findByOrganiserId(organiserEntity.get().getId());
            organiserReviewStatsEntity.ifPresent(eventOrganiserReviewStatsEntity -> eventOrganiserMapper.updateOrganiserFromStats(eventOrganiserReviewStatsEntity, organiserModel));

            mediaUtilities.lookupThumbnailImage(organiserModel, ObjectType.ORGANISER, organiserModel.getId());
            organiserEntity.ifPresent(eventOrganiserEntity -> entity.setOrganiser(organiserModel));
        }
    }

    public boolean checkIfUserAttendedEvent(Integer entity, Integer userId){
       return userAttendedEventFacade.checkIfUserAttendedEvent(userId, entity);
    }

    public void updateEventTagsBulk(EventUpdateModel eventUpdateModel, EventEntity entity){
        if (eventUpdateModel.getTagIds() != null) {
            eventTagsDAO.deleteAllByEventId(entity.getId());

            List<EventTagsEntity> newTags = new ArrayList<>();
            for (Integer tagId : eventUpdateModel.getTagIds()) {
                EventTagsEntity eventTag = new EventTagsEntity();
                eventTag.setEventId(entity.getId());
                eventTag.setTagId(tagId);
                newTags.add(eventTag);
            }
            if (!newTags.isEmpty()) {
                eventTagsDAO.saveAllAndFlush(newTags);
            }
        }
    }

    /**
     * Adds new event tags for a newly created event
     *
     * @param tagIds the list of tag IDs to add to the event
     * @param eventId the ID of the event
     */
    public void addEventTags(List<Integer> tagIds, Integer eventId) {
        if (tagIds == null || tagIds.isEmpty()) return;

        List<EventTagsEntity> tags = new ArrayList<>();
        for (Integer tagId : tagIds) {
            EventTagsEntity eventTag = new EventTagsEntity();
            eventTag.setEventId(eventId);
            eventTag.setTagId(tagId);
            tags.add(eventTag);
        }
        eventTagsDAO.saveAllAndFlush(tags);
    }
}

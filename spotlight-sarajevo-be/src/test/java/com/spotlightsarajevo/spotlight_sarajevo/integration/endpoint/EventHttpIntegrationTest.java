package com.spotlightsarajevo.spotlight_sarajevo.integration.endpoint;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.exceptions.EventExceptions;
import com.spotlightsarajevo.modules.event.api.dto.EventCreateModel;
import com.spotlightsarajevo.modules.event.api.dto.EventModel;
import com.spotlightsarajevo.modules.event.api.dto.EventOverviewModel;
import com.spotlightsarajevo.modules.event.domain.EventDAO;
import com.spotlightsarajevo.modules.event.service.EventService;
import com.spotlightsarajevo.spotlight_sarajevo.integration.BaseIntegrationClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EventHttpIntegrationTest extends BaseIntegrationClass {
    @MockitoBean
    private EventService eventService;
    @Mock
    private EventDAO eventDAO;
    @Mock
    private Principal principal;

    @Test
    void findEventOverview_validSlug_returns200() throws Exception {
        when(eventService.findEventOverview("test-event"))
                .thenReturn(ResponseEntity.ok(new EventOverviewModel()));

        mockMvc.perform(get("/event/find-event-overview")
                        .param("eventSlug", "test-event"))
                .andExpect(status().isOk());
    }

    @Test
    void findEventOverview_invalidSlug_returns404() throws Exception {
        when(eventService.findEventOverview("randomThing"))
                .thenThrow(new EventExceptions.EventNotFoundException(
                        ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND));

        mockMvc.perform(get("/event/find-event-overview")
                        .param("eventSlug", "randomThing"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findEventOverview_missingSlug_returns400() throws Exception {
        mockMvc.perform(get("/event/find-event-overview")).andExpect(status().isBadRequest());
    }

    @Test
    void create_validModel_returnsCreatedModelAnd200() throws Exception {
        EventModel model = new EventModel();

        when(eventService.create(any(EventCreateModel.class), any()))
                .thenReturn(ResponseEntity.ok(model));

        EventCreateModel request = new EventCreateModel();

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void create_nullBody_returns400() throws Exception {
        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_missingContentType_returns415() throws Exception {
        mockMvc.perform(post("/event")
                        .content(objectMapper.writeValueAsString(new EventCreateModel())))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void delete_validId_returns200() throws Exception {
        when(eventService.delete(1))
                .thenReturn(ResponseEntity.ok(new EventModel()));

        mockMvc.perform(delete("/event/1"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_invalidId_returns404() throws Exception {
        when(eventService.delete(999))
                .thenThrow(new EventExceptions.EventNotFoundException(
                        ExceptionCodes.EventExceptionCodes.EVENT_NOT_FOUND));

        mockMvc.perform(delete("/event/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findEventsPaginated_defaultParams_returns200() throws Exception {
        when(eventService.findEventsPaginated(any(PageRequest.class), any(), any(), any()))
                .thenReturn(ResponseEntity.ok(Page.empty()));

        mockMvc.perform(get("/event/find-events"))
                .andExpect(status().isOk());
    }

    @Test
    void findEventsOnDay_validDate_returns200() throws Exception {
        when(eventService.findEventsForDate(any(LocalDate.class)))
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/event/events-on-day/2026-06-11"))
                .andExpect(status().isOk());
    }

    @Test
    void findEventsOnDay_invalidDateFormat_returns400() throws Exception {
        mockMvc.perform(get("/event/events-on-day/not-a-date"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void checkEventDatesForMonth_validParams_returns200() throws Exception {
        when(eventService.checkEventDatesForMonth(2026, 6))
                .thenReturn(ResponseEntity.ok(Map.of()));

        mockMvc.perform(get("/event/event-dates-check")
                        .param("year", "2026")
                        .param("month", "6"))
                .andExpect(status().isOk());
    }

    @Test
    void checkEventDatesForMonth_missingParams_returns400() throws Exception {
        mockMvc.perform(get("/event/event-dates-check"))
                .andExpect(status().isBadRequest());
    }
}

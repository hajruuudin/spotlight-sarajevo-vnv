package com.spotlightsarajevo.modules.event.api;

import com.spotlightsarajevo.modules.event.api.dto.*;
import com.spotlightsarajevo.modules.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "event", description = "Event API")
@AllArgsConstructor
@RequestMapping(value = "/event")
public class EventRESTController {
    EventService eventService;

    @GetMapping(value = "/find-events")
    @Operation(description = "Retrieve a paginated result of events based on specific criteria")
    public ResponseEntity<Page<EventShorthandModel>> findEventsPaginated(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "ALPHABETICAL", required = false) String sortOption,
            @RequestParam(required = false) List<Integer> categoryIds) {
        return eventService.findEventsPaginated(PageRequest.of(pageNumber, pageSize), searchTerm, sortOption, categoryIds);
    }

    @GetMapping(value = "/events-on-day/{date}")
    @Operation(description = "Retrieve all events for a specific date (calendar view)")
    public ResponseEntity<List<EventShorthandModel>> findEventsPerDay(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return eventService.findEventsForDate(date);
    }

    @GetMapping(value = "/event-dates-check")
    @Operation(description = "Check which dates in a given month have events")
    public ResponseEntity<Map<LocalDate, Boolean>> checkEventDatesForMonth(
            @RequestParam(required = true) Integer year,
            @RequestParam(required = true) Integer month
    ) {
        return eventService.checkEventDatesForMonth(year, month);
    }

    @GetMapping(value = "/find-event-overview")
    @Operation(description = "Get an overview model for an event, including category names, tags and organiser model")
    public ResponseEntity<EventOverviewModel> findEventOverview(
            @RequestParam(required = true) String eventSlug
    ){
        return eventService.findEventOverview(eventSlug);
    }

    @GetMapping(value = "/attended-event/check")
    @Operation(description = "Check the status of the event and if the user has labeled it as attended/will attend")
    public ResponseEntity<Boolean> getEventStatus(
            @RequestParam(required = true) Integer eventId,
            Principal principal
    ){
        return eventService.getEventAttendedStatus(eventId, principal);
    }

    @GetMapping(value = "/organisers/all")
    @Operation(description = "Find all event organisers (used mostly in the Add Event and Edit Event Functions on the FE)")
    public ResponseEntity<List<EventOrganiserModel>> getAllOrganisers(){
        return eventService.findAllOrganisers();
    }

    @PostMapping(value = "/organiser")
    @Operation(description = "Create an event organiser object - Used on the Admin Add Event Option")
    public ResponseEntity<EventOrganiserModel> createEventOrganiser(
            @RequestBody EventOrganiserCreateModel request,
            Principal principal
    ){
        return eventService.createEventOrganiser(request, principal);
    }

    @PutMapping
    @Operation(description = "Update a whole Entity Overview within the system")
    public ResponseEntity<EventModel> updateEvent(
            @RequestBody EventUpdateModel eventUpdateModel,
            Principal principal
    ){
        return eventService.update(eventUpdateModel, principal);
    }

    @PostMapping
    @Operation(description = "Create a new event in the system")
    public ResponseEntity<EventModel> createEvent(
            @RequestBody EventCreateModel eventCreateModel,
            Principal principal
    ){
        return eventService.create(eventCreateModel, principal);
    }

    @PutMapping(value = "/organiser")
    @Operation(description = "Update an event organiser within the system")
    public ResponseEntity<EventOrganiserModel> updateOrganiserEntity(
            @RequestBody EventOrganiserUpdateModel request,
            Principal principal
    ){
        return eventService.update(request, principal);
    }

    @GetMapping(value = "/admin/count")
    @Operation(description = "Retrieve the total count of events in the system (admin-only)")
    public ResponseEntity<Long> getEventsTotalCount(Principal principal) {
        return eventService.getEventsTotalCount(principal);
    }

    @GetMapping(value = "/admin/recently-added")
    @Operation(description = "Retrieve recently added events for admin dashboard")
    public ResponseEntity<List<EventShorthandModel>> getRecentlyAddedEvents(
            @RequestParam(defaultValue = "5", required = false) Integer limit,
            Principal principal
    ) {
        return eventService.getRecentlyAddedEvents(limit, principal);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(description = "Delete an event from the database")
    public ResponseEntity<EventModel> delete(
            @PathVariable Integer id
    ){
        return eventService.delete(id);
    }
}

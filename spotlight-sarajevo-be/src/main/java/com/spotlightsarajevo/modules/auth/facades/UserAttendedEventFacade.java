package com.spotlightsarajevo.modules.auth.facades;

/**
 * Facade for checking if a user has attended a specific event.
 */
public interface UserAttendedEventFacade {
    /**
     * Checks if a user has attended a specific event.
     *
     * @param userId  the ID of the user
     * @param eventId the ID of the event
     * @return true if the user has attended the event, false otherwise
     */
    boolean checkIfUserAttendedEvent(Integer userId, Integer eventId);
}

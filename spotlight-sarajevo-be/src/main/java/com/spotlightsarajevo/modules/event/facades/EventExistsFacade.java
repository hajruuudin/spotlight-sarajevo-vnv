package com.spotlightsarajevo.modules.event.facades;

/**
 * Facade for checking the existence of an event.
 */
public interface EventExistsFacade {
    /**
     * Checks if an event with the given ID exists.
     *
     * @param eventId the ID of the event
     * @return true if the event exists, false otherwise
     */
    boolean eventExists(Integer eventId);
}

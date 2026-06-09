package com.spotlightsarajevo.modules.spot.facades;

/**
 * Facade for checking the existence of a spot.
 */
public interface SpotExistsFacade {
    /**
     * Checks if a spot with the given ID exists.
     *
     * @param spotId the ID of the spot
     * @return true if the spot exists, false otherwise
     */
    boolean spotExists(Integer spotId);
}

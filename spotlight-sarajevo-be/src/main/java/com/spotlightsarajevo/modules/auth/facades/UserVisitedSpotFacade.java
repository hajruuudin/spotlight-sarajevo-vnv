package com.spotlightsarajevo.modules.auth.facades;

/**
 * Facade for checking if a user has visited a specific spot.
 */
public interface UserVisitedSpotFacade {
    /**
     * Checks if a user has visited a specific spot.
     *
     * @param userId the ID of the user
     * @param spotId the ID of the spot
     * @return true if the user has visited the spot, false otherwise
     */
    boolean checkIfUserVisitedSpot(Integer userId, Integer spotId);
}

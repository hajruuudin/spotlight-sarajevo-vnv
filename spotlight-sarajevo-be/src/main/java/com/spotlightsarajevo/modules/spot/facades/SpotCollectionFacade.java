package com.spotlightsarajevo.modules.spot.facades;

import com.spotlightsarajevo.modules.spot.api.dto.SpotShorthandModel;

/**
 * Facade for retrieving shorthand information about spots.
 */
public interface SpotCollectionFacade {
    /**
     * Retrieves shorthand information for a specific spot by its ID.
     *
     * @param spotId the ID of the spot
     * @return SpotShorthandModel containing shorthand information about the spot
     */
    SpotShorthandModel getSpotShorthand(Integer spotId);
}

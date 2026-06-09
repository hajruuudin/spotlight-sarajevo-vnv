package com.spotlightsarajevo.modules.spot.facades;

import com.spotlightsarajevo.common.enums.ExceptionCodes;
import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.exceptions.SpotExceptions;
import com.spotlightsarajevo.modules.media.utils.MediaUtilities;
import com.spotlightsarajevo.modules.spot.api.dto.SpotShorthandModel;
import com.spotlightsarajevo.modules.spot.domain.SpotDAO;
import com.spotlightsarajevo.modules.spot.domain.entity.SpotEntity;
import com.spotlightsarajevo.modules.spot.mapper.SpotMapper;
import com.spotlightsarajevo.modules.spot.utils.SpotUtilities;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SpotCollectionFacadeImpl implements SpotCollectionFacade{
    private final SpotDAO spotDAO;
    private final SpotMapper spotMapper;
    private final SpotUtilities spotUtilities;
    private final MediaUtilities mediaUtilities;

    @Override
    public SpotShorthandModel getSpotShorthand(Integer spotId) {
        SpotEntity spot = spotDAO.findById(spotId)
                .orElseThrow(() -> new SpotExceptions.SpotNotFoundException(
                        ExceptionCodes.SpotExceptionCodes.SPOT_NOT_FOUND
                ));

        spotUtilities.setSpotTags(spot);
        spotUtilities.setSpotCategoriesAndRating(spot);
        SpotShorthandModel dto = spotMapper.entityToShorthandDto(spot);

        mediaUtilities.lookupThumbnailImage(dto, ObjectType.SPOT, dto.getId());
        return dto;
    }
}

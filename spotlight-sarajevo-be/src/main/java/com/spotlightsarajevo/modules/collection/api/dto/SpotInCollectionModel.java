package com.spotlightsarajevo.modules.collection.api.dto;

import com.spotlightsarajevo.common.utils.CollectionItemsObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SpotInCollectionModel implements Serializable, CollectionItemsObject {
    @Schema(description = "List of IDs of the collections the spot is located id")
    private List<Integer> ids;
}

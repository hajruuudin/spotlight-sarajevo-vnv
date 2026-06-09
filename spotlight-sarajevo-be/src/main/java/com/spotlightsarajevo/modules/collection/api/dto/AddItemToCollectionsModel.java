package com.spotlightsarajevo.modules.collection.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AddItemToCollectionsModel implements Serializable {
    private List<Integer> ids;
    private String collectionType;
    private Integer objectId;
    private Boolean isSystem;
}

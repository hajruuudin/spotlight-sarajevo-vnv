package com.spotlightsarajevo.modules.tag.service;

import com.spotlightsarajevo.modules.tag.api.dto.TagModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * TagService interface defines methods for retrieving tag-related data.
 */
public interface TagService {
    /**
     * Retrieves a list of all tags.
     *
     * @return ResponseEntity containing a list of TagModel
     */
    ResponseEntity<List<TagModel>> findAll();
}

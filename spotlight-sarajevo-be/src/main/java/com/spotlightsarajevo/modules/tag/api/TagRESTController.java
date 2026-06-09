package com.spotlightsarajevo.modules.tag.api;

import com.spotlightsarajevo.modules.tag.api.dto.TagModel;
import com.spotlightsarajevo.modules.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "tag", description = "Tag API")
@AllArgsConstructor
@RequestMapping(value = "/tag")
public class TagRESTController {
    TagService tagService;

    @GetMapping(value = "/all")
    @Operation(description = "Get all tags from the system")
    public ResponseEntity<List<TagModel>> findAll() {
        return this.tagService.findAll();
    }
}

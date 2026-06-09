package com.spotlightsarajevo.modules.tag.service;

import com.spotlightsarajevo.modules.tag.api.dto.TagModel;
import com.spotlightsarajevo.modules.tag.domain.TagDAO;
import com.spotlightsarajevo.modules.tag.domain.entity.TagEntity;
import com.spotlightsarajevo.modules.tag.mappers.TagMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {
    TagDAO tagDAO;
    TagMapper tagMapper;

    @Override
    public ResponseEntity<List<TagModel>> findAll() {
        List<TagEntity> entities = tagDAO.findAll();
        List<TagModel> models = tagMapper.entitiesToDtos(entities);

        return ResponseEntity.status(200).body(models);
    }
}

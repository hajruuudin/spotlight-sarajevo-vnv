package com.spotlightsarajevo.modules.collection.domain;

import com.spotlightsarajevo.common.utils.CollectionItem;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEventEntity;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionSpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionSpotDAO extends JpaRepository<CollectionSpotEntity, Integer> {
    List<CollectionItem> findAllByCollectionId(Integer collectionId);
    Optional<CollectionSpotEntity> findByCollectionIdAndSpotId(Integer collectionId, Integer spotId);
    List<CollectionSpotEntity> findAllBySpotIdAndUserId(Integer spotId, Integer userId);
    List<CollectionSpotEntity> findAllByUserId(Integer userId);
    boolean existsByCollectionIdAndSpotId(Integer collectionId, Integer spotId);
}

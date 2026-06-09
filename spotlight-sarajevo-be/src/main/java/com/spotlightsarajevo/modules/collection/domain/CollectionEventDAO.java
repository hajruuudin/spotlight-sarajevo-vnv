package com.spotlightsarajevo.modules.collection.domain;

import com.spotlightsarajevo.common.utils.CollectionItem;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEventEntity;
import com.spotlightsarajevo.modules.collection.domain.entity.CollectionSpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionEventDAO extends JpaRepository<CollectionEventEntity, Integer> {
    List<CollectionItem> findAllByCollectionId(Integer collectionId);
    Optional<CollectionEventEntity> findByCollectionIdAndEventId(Integer collectionId, Integer eventId);
    List<CollectionEventEntity> findAllByEventIdAndUserId(Integer eventId, Integer userId);
    List<CollectionEventEntity> findAllByUserId(Integer userId);
    boolean existsByCollectionIdAndEventId(Integer collectionId, Integer eventId);
}

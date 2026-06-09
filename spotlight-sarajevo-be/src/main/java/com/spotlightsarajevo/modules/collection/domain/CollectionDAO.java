package com.spotlightsarajevo.modules.collection.domain;

import com.spotlightsarajevo.modules.collection.domain.entity.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionDAO extends JpaRepository<CollectionEntity, Integer> {
    List<CollectionEntity> findByUserId(Integer userId);
    Optional<CollectionEntity> findByUserIdAndCollectionTypeAndIsSystemTrue(Integer userId, String collectionType);
}

package com.spotlightsarajevo.modules.collection.domain.entity;

import com.spotlightsarajevo.common.utils.CollectionItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_collection_spot")
public class CollectionSpotEntity implements Serializable, CollectionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "collection_id", nullable = false)
    private Integer collectionId;

    @Column(name = "spot_id", nullable = false)
    private Integer spotId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "collection_id", insertable = false, updatable = false)
//    CollectionEntity collection;
//
//    public Integer getUserId() {
//        return this.collection != null ? this.collection.getUserId() : null;
//    }
}

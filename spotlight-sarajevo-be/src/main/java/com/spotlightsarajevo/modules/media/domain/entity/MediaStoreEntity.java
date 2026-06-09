package com.spotlightsarajevo.modules.media.domain.entity;

import com.spotlightsarajevo.common.enums.ObjectType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_media_store")
public class MediaStoreEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "item_id")
    private Integer itemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_category", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private ObjectType itemCategory;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "image_delete_url", columnDefinition = "TEXT")
    private String imageDeleteUrl;

    @Column(name = "is_thumbnail")
    private Boolean isThumbnail;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;
}

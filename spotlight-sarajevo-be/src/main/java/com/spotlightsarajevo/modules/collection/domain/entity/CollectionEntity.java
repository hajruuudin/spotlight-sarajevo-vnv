package com.spotlightsarajevo.modules.collection.domain.entity;

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
@Table(name = "ss_collection")
public class CollectionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "collection_name", columnDefinition = "VARCHAR", length = 30, nullable = false)
    private String collectionName;

    @Column(name = "collection_description", columnDefinition = "VARCHAR", length = 60)
    private String collectionDescription;

    @Column(name = "collection_type", columnDefinition = "VARCHAR", length = 30, nullable = false)
    private String collectionType;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "created_by", columnDefinition = "VARCHAR", length = 255)
    private String createdBy;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "is_system", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isSystem;
}

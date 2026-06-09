package com.spotlightsarajevo.modules.guide.domain.entity;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_tourist_guide_section")
public class TouristGuideSectionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "guide_id", nullable = false)
    private Integer guideId;

    @Column(name = "section_title_bs", nullable = false, length = 100)
    private String sectionTitleBs;

    @Column(name = "section_title_en", nullable = false, length = 100)
    private String sectionTitleEn;

    @Column(name = "section_body_bs", columnDefinition = "TEXT", nullable = false)
    private String sectionBodyBs;

    @Column(name = "section_body_en", columnDefinition = "TEXT", nullable = false)
    private String sectionBodyEn;

    @Column(name = "order_idx", columnDefinition = "INTEGER DEFAULT 0")
    private Integer orderIdx;

    @Transient
    private MediaStoreModel thumbnailImage;
}
package com.spotlightsarajevo.modules.guide.domain.entity;

import com.spotlightsarajevo.common.enums.GuideType;
import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Map;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_tourist_guide")
public class TouristGuideEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "slug", columnDefinition = "VARCHAR", nullable = false)
    private String slug;

    @Column(name = "guide_title_bs", columnDefinition = "VARCHAR", length = 100, nullable = false)
    private String guideTitleBs;

    @Column(name = "guide_title_en", columnDefinition = "VARCHAR", length = 100, nullable = false)
    private String guideTitleEn;

    @Column(name = "guide_small_description_bs", columnDefinition = "VARCHAR", length = 250, nullable = false)
    private String guideSmallDescriptionBs;

    @Column(name = "guide_small_description_en", columnDefinition = "VARCHAR", length = 250, nullable = false)
    private String guideSmallDescriptionEn;

    @Column(name = "guide_full_description_bs", columnDefinition = "TEXT", nullable = false)
    private String guideFullDescriptionBs;

    @Column(name = "guide_full_description_en", columnDefinition = "TEXT", nullable = false)
    private String guideFullDescriptionEn;

    @Enumerated(EnumType.STRING)
    @Column(name = "guide_type", columnDefinition = "VARCHAR", length = 20, nullable = false)
    private GuideType guideType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contact_info")
    private Map<String, String> contactInfo;

    @Column(name = "category_id", columnDefinition = "NUMERIC", nullable = false)
    private Integer categoryId;

    @Transient
    private String categoryNameBs;

    @Transient
    private String categoryNameEn;

    @Transient
    private MediaStoreModel thumbnailImage;
}

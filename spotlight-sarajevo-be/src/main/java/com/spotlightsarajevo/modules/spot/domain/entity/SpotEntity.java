package com.spotlightsarajevo.modules.spot.domain.entity;

import com.spotlightsarajevo.modules.media.api.dto.MediaStoreModel;
import com.spotlightsarajevo.modules.review.api.dto.SpotReviewModel;
import com.spotlightsarajevo.modules.spot.api.dto.SpotWorkHoursModel;
import com.spotlightsarajevo.modules.tag.api.dto.TagModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_spot")
public class SpotEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "official_name_bs", columnDefinition = "VARCHAR", length = 40)
    private String officialNameBs;

    @Column(name = "official_name_en", columnDefinition = "VARCHAR", length = 40)
    private String officialNameEn;

    @Column(name = "small_description_bs", columnDefinition = "VARCHAR", length = 60)
    private String smallDescriptionBs;

    @Column(name = "small_description_en", columnDefinition = "VARCHAR", length = 60)
    private String smallDescriptionEn;

    @Column(name = "full_description_bs", columnDefinition = "TEXT")
    private String fullDescriptionBs;

    @Column(name = "full_description_en", columnDefinition = "TEXT")
    private String fullDescriptionEn;

    @Column(name = "latitude", columnDefinition = "NUMERIC")
    private BigDecimal latitude;

    @Column(name = "longitude", columnDefinition = "NUMERIC")
    private BigDecimal longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified")
    private LocalDateTime modified;

    @Column(name = "modified_by")
    private String modifiedBy;

    // Full Review Stats Object
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "spot_id", insertable = false, updatable = false)
    private SpotReviewStatsEntity reviewStats;

    @Transient
    private String categoryNameBs;

    @Transient
    private String categoryNameEn;

    @Transient
    private BigDecimal combinedRating;

    @Transient
    private MediaStoreModel thumbnailImage;

    @Transient
    private List<MediaStoreModel> images;

    @Transient
    private List<TagModel> spotTags;

    @Transient
    private List<SpotWorkHoursModel> workHours;

    @Transient
    private List<SpotReviewModel> reviews;
}

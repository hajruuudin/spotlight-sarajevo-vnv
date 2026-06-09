package com.spotlightsarajevo.modules.category.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_spot_category")
public class SpotCategoryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "spot_category_name_bs", columnDefinition = "VARCHAR", length = 255, unique = true, nullable = false)
    private String spotCategoryNameBs;

    @Column(name = "spot_category_name_en", columnDefinition = "VARCHAR", length = 255, unique = true, nullable = false)
    private String spotCategoryNameEn;

    @Column(name = "spot_category_description_bs", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private String spotCategoryDescriptionBs;

    @Column(name = "spot_category_description_en", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private String spotCategoryDescriptionEn;

    @Column(name = "spot_category_color_code", columnDefinition = "VARCHAR", length = 9)
    private String spotCategoryColorCode;
}

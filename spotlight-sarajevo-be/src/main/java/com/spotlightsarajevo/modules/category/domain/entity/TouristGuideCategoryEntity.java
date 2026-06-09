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
@Table(name = "ss_tourist_guide_category")
public class TouristGuideCategoryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name_bs", nullable = false, length = 100)
    private String categoryNameBs;

    @Column(name = "category_name_en", nullable = false, length = 100)
    private String categoryNameEn;

    @Column(name = "category_desc_bs", columnDefinition = "TEXT")
    private String categoryDescBs;

    @Column(name = "category_desc_en", columnDefinition = "TEXT")
    private String categoryDescEn;

    @Column(name = "color_code", nullable = false, length = 7)
    private String colorCode;
}

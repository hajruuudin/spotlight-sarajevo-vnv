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
@Table(name = "ss_event_category")
public class EventCategoryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "event_category_name_bs", columnDefinition = "VARCHAR", length = 255, unique = true, nullable = false)
    private String eventCategoryNameBs;

    @Column(name = "event_category_name_en", columnDefinition = "VARCHAR", length = 255, unique = true, nullable = false)
    private String eventCategoryNameEn;

    @Column(name = "event_category_description_bs", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private String eventCategoryDescriptionBs;

    @Column(name = "event_category_description_en", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private String eventCategoryDescriptionEn;

    @Column(name = "event_category_color_code", columnDefinition = "VARCHAR", length = 9)
    private String eventCategoryColorCode;
}

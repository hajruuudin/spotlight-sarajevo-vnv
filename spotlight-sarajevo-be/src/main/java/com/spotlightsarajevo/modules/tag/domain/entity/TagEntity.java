package com.spotlightsarajevo.modules.tag.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_tag")
public class TagEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tag_name_bs", columnDefinition = "VARCHAR", length = 255, unique = true, nullable = false)
    private String tagNameBs;

    @Column(name = "tag_name_en", columnDefinition = "VARCHAR", length = 255, unique = true, nullable = false)
    private String tagNameEn;

    @Column(name = "tag_description_bs", columnDefinition = "VARCHAR", length = 255)
    private String tagDescriptionBs;

    @Column(name = "tag_description_en", columnDefinition = "VARCHAR", length = 255)
    private String tagDescriptionEn;
}

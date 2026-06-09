package com.spotlightsarajevo.modules.event.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_event_organiser")
public class EventOrganiserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "organiser_name", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private String organiserName;

    @Column(name = "organiser_creation_date", nullable = false)
    private LocalDate organiserCreationDate;

    @Column(name = "organiser_category_id", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private Integer organiserCategoryId;

    @Column(name = "organiser_phone", columnDefinition = "VARCHAR", length = 20)
    private String organiserPhone;

    @Column(name = "organiser_email", columnDefinition = "VARCHAR", length = 255)
    private String organiserEmail;

    @Column(name = "organiser_website", columnDefinition = "VARCHAR", length = 255)
    private String organiserWebsite;
}

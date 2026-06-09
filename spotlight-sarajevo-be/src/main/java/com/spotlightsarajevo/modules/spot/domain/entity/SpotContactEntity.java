package com.spotlightsarajevo.modules.spot.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_spot_contact")
public class SpotContactEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "spot_contact_phone", columnDefinition = "VARCHAR", length = 20)
    private String spotContactPhone;

    @Column(name = "spot_contact_email", columnDefinition = "VARCHAR", length = 255)
    private String spotContactEmail;

    @Column(name = "spot_contact_webpage", columnDefinition = "VARCHAR", length = 255)
    private String spotContactWebpage;
}

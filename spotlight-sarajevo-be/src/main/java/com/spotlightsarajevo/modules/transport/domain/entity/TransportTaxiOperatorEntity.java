package com.spotlightsarajevo.modules.transport.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "ss_transport_taxi_info")
@AllArgsConstructor
@NoArgsConstructor
public class TransportTaxiOperatorEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "company", nullable = false, columnDefinition = "VARCHAR")
    private String company;

    @Column(name = "phone", columnDefinition = "VARCHAR")
    private String phone;

    @Column(name = "website", columnDefinition = "VARCHAR")
    private String website;
}

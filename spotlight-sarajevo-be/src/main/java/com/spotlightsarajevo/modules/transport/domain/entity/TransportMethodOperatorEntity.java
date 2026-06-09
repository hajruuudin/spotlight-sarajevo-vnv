package com.spotlightsarajevo.modules.transport.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_transport_method_operator")
public class TransportMethodOperatorEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "transport_operator_name", columnDefinition = "VARCHAR", length = 30, nullable = false)
    private String transportOperatorName;

    @Column(name = "transport_operator_webpage", columnDefinition = "VARCHAR", length = 255)
    private String transportOperatorWebpage;

    @Column(name = "transport_operator_phone", columnDefinition = "VARCHAR", length = 255)
    private String transportOperatorPhone;

    @Column(name = "transport_operator_email", columnDefinition = "VARCHAR", length = 255)
    private String transportOperatorEmail;
}

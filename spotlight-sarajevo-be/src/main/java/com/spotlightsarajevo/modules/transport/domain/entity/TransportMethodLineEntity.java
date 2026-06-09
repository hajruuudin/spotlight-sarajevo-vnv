package com.spotlightsarajevo.modules.transport.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_transport_method_line")
public class TransportMethodLineEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id", nullable = false)
    private TransportMethodOperatorEntity operator;

    @Column(name = "line_start", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private String lineStart;

    @Column(name = "line_end", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private String lineEnd;

    @Column(name = "line_number", columnDefinition = "VARCHAR", length = 50, nullable = false)
    private String lineNumber;

    @Column(name = "geometry", columnDefinition = "GEOMETRY")
    private Geometry geometry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_type_id", nullable = false)
    private TransportMethodEntity transportType;
}

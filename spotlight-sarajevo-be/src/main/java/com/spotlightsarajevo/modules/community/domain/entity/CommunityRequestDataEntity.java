package com.spotlightsarajevo.modules.community.domain.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_community_request_pending_info")
public class CommunityRequestDataEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "community_request_id", nullable = false)
    private Integer communityRequestId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "pending_info", columnDefinition = "JSONB", length = 255, nullable = false)
    private JsonNode pendingInfo;
}

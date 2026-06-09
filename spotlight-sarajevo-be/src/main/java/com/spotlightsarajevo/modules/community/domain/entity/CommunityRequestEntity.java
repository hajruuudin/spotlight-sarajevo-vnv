package com.spotlightsarajevo.modules.community.domain.entity;

import com.spotlightsarajevo.common.enums.ObjectType;
import com.spotlightsarajevo.common.enums.RequestStatus;
import com.spotlightsarajevo.common.enums.RequestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_community_request")
public class CommunityRequestEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "request_type", columnDefinition = "request_type_enum", nullable = false)
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "object_type", columnDefinition = "object_type_enum", nullable = false)
    private ObjectType objectType;

    @Column(name = "request_header", columnDefinition = "VARCHAR", length = 30, nullable = false)
    private String requestHeader;

    @Column(name = "request_description", columnDefinition = "VARCHAR", length = 255, nullable = false)
    private String requestDescription;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "request_status_enum", nullable = false)
    private RequestStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

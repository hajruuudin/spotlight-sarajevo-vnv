package com.spotlightsarajevo.modules.auth.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ss_user_favourite_events")
public class UserFavouriteEventsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id", columnDefinition = "NUMERIC")
    private Integer userId;

    @Column(name = "event_id", columnDefinition = "NUMERIC")
    private Integer eventId;
}

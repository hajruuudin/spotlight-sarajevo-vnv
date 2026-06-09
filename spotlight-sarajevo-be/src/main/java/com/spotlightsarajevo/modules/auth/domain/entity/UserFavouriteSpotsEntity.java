package com.spotlightsarajevo.modules.auth.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "ss_user_favourite_spots")
@AllArgsConstructor
@NoArgsConstructor
public class UserFavouriteSpotsEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id", columnDefinition = "NUMERIC")
    private Integer userId;

    @Column(name = "spot_id", columnDefinition = "NUMERIC")
    private Integer spotId;
}

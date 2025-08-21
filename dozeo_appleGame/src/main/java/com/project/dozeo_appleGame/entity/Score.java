package com.project.dozeo_appleGame.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(
        name = "score",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "game_type"})
        }
)
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "game_type", nullable = false)
    private String gameType;

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

}

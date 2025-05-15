package com.project.dozeo_appleGame.entity.score;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Score {

    @Id
    @GeneratedValue
    private Long id;
    private String nickName;
    private int points;
    private LocalDateTime datePlayed;

}

package com.project.dozeo_appleGame.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreRequest {
    private Long userId;
    private String gameType;
    private int points;
}
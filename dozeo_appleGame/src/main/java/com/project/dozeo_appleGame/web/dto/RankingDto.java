package com.project.dozeo_appleGame.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingDto {
    private String nickname;
    private int ranking;
    private int points;
}

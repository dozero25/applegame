package com.project.dozeo_appleGame.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnlinkRequestDto {
    private String provider;
    private String accessToken;
    private Long id;
}

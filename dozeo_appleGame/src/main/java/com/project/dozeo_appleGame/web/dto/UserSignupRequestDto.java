package com.project.dozeo_appleGame.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupRequestDto {
    private String username;
    private String password;
    private String email;
    private String nickname;
}

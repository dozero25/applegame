package com.project.dozeo_appleGame.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "naver")
public class NaverProperties {

    private String clientId;
    private String clientSecret;
}

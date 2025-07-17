package com.project.dozeo_appleGame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
}

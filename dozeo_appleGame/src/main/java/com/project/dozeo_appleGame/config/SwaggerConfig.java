package com.project.dozeo_appleGame.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                .title("appleGame API")
                .description("사과게임 사이드 프로젝트입니다.")
                .version("1.0.0"));

    }

}

package com.example.deckbuilder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:3000",  // Desarrollo local
                    "http://localhost:8080",  // Desarrollo local
                    "http://s3-realmsindiscord-react-fullstack.s3-website-us-east-1.amazonaws.com",  // ← TU BUCKET S3
                    "https://s3-realmsindiscord-react-fullstack.s3-website-us-east-1.amazonaws.com"  // ← TU BUCKET S3 con HTTPS
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
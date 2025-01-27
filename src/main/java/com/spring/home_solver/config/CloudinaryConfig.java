package com.spring.home_solver.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${spring.cloudinary.name}")
    private String CLOUDINARY_NAME;

    @Value("${spring.cloudinary.api.key}")
    private String CLOUDINARY_API_KEY;

    @Value("${spring.cloudinary.api.secret}")
    private String CLOUDINARY_API_SECRET;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name",CLOUDINARY_NAME);
        config.put("api_key",CLOUDINARY_API_KEY);
        config.put("api_secret",CLOUDINARY_API_SECRET);
        return new Cloudinary(config);
    }
}

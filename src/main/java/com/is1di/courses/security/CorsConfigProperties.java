package com.is1di.courses.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "spring.security.cors")
@Component
public class CorsConfigProperties {
    private List<String> allowedOrigins;
}

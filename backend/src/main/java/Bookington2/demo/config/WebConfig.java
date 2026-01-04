package Bookington2.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS policy to all endpoints
                .allowedOriginPatterns("*")// Allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // HTTP methods
                .allowedHeaders("Authorization", "Content-Type") // Allowed headers
                .allowCredentials(true); // Allow credentials (cookies, Authorization headers, etc.)
    }
}

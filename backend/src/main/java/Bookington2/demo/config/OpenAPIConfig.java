package Bookington2.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🏸 Bookington API")
                        .version("1.0.0")
                        .description("API Documentation cho hệ thống đặt sân cầu lông Bookington. " +
                                "Bao gồm các module: Owner (Chủ sân), Player (Người chơi), Admin.")
                        .contact(new Contact()
                                .name("Bookington Team")
                                .email("support@bookington.com")
                                .url("https://bookington.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development"),
                        new Server().url("https://api.bookington.com").description("Production")
                ));
    }
}


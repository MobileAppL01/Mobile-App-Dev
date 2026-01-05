package Bookington2.demo.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private List<String> roles;
}

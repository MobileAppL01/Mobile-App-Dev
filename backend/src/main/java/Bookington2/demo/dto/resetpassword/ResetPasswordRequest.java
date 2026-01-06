package Bookington2.demo.dto.resetpassword;

import lombok.Data;

@Data
public class ResetPasswordRequest {
  private String email;
  private String phoneNumber;
}

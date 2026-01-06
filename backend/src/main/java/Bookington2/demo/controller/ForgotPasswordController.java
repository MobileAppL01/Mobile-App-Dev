package Bookington2.demo.controller;

import Bookington2.demo.dto.resetpassword.ResetPasswordRequest;
import Bookington2.demo.service.ResetPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ForgotPasswordController {
    private final ResetPasswordService resetPasswordService;

    public ForgotPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/reset-password")
    ResponseEntity<String> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return resetPasswordService.resetForgotPassword(resetPasswordRequest.getEmail());
    }
}

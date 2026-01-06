package Bookington2.demo.controller;

import Bookington2.demo.dto.resetpassword.ChangePasswordRequest;
import Bookington2.demo.dto.resetpassword.ResetPasswordRequest;
import Bookington2.demo.service.ResetPasswordService;
import Bookington2.demo.service.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ForgotPasswordController {
    private final ResetPasswordService resetPasswordService;
    private Integer getCurrentUserId() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        return ((UserDetailsImpl) auth.getPrincipal()).getId();
    }
    public ForgotPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/reset-password")
    ResponseEntity<String> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return resetPasswordService.resetForgotPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getPhoneNumber());
    }
    @PostMapping("/change-password")
    ResponseEntity<String> changePassword(ChangePasswordRequest changePasswordRequest) {
        return resetPasswordService.resetPassword(getCurrentUserId(),changePasswordRequest.getOldPassword(),changePasswordRequest.getNewPassword());
    }
}

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return ((UserDetailsImpl) auth.getPrincipal()).getId();
    }

    public ForgotPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/auth/forgot-password/send-otp")
    ResponseEntity<String> sendOtp(@RequestBody ResetPasswordRequest request) {
        return resetPasswordService.sendForgotPasswordOtp(request.getEmail());
    }

    @PostMapping("/auth/forgot-password/verify-otp")
    ResponseEntity<String> verifyOtp(@RequestBody ResetPasswordRequest request) {
        return resetPasswordService.verifyOtp(request.getEmail(), request.getOtp());
    }

    @PostMapping("/auth/reset-password-otp")
    ResponseEntity<String> resetPasswordWithOtp(@RequestBody ResetPasswordRequest request) {
        return resetPasswordService.resetPasswordWithOtp(request.getEmail(), request.getOtp(),
                request.getNewPassword());
    }

    @PostMapping("/change-password")
    ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return resetPasswordService.resetPassword(getCurrentUserId(), changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword());
    }
}

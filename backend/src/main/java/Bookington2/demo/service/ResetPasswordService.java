package Bookington2.demo.service;

import Bookington2.demo.entity.User;
import Bookington2.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResetPasswordService {
    private final Logger log = LoggerFactory.getLogger(ResetPasswordService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public ResetPasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public ResponseEntity<String> resetPassword(Integer userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Đổi mật khẩu thành công");
        } else {
            return ResponseEntity.badRequest().body("Mật khẩu cũ không chính xác");
        }
    }

    // Step 1: Send OTP
    public ResponseEntity<String> sendForgotPasswordOtp(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("Email không tồn tại trong hệ thống");
        }

        // Generate 6-digit OTP
        String otp = String.format("%06d", new java.security.SecureRandom().nextInt(999999));

        user.setVerificationCode(otp);
        user.setVerificationCodeExpiresAt(java.time.LocalDateTime.now().plusMinutes(5)); // Valid for 5 minutes
        userRepository.save(user);

        sendEmail(user.getEmail(), otp, true);
        return ResponseEntity.ok("Mã OTP đã được gửi tới email của bạn");
    }

    // Step 2: Verify OTP
    public ResponseEntity<String> verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("Email không hợp lệ");
        }

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(otp)) {
            return ResponseEntity.badRequest().body("Mã OTP không chính xác");
        }

        if (user.getVerificationCodeExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Mã OTP đã hết hạn");
        }

        return ResponseEntity.ok("Xác thực OTP thành công");
    }

    // Step 3: Reset Password
    public ResponseEntity<String> resetPasswordWithOtp(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("Email không hợp lệ");
        }

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(otp)) {
            return ResponseEntity.badRequest().body("Mã OTP không chính xác");
        }

        if (user.getVerificationCodeExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Mã OTP đã hết hạn");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);

        return ResponseEntity.ok("Đặt lại mật khẩu thành công");
    }

    private void sendEmail(String to, String content, boolean isOtp) {
        try {
            jakarta.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();
            org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(
                    mimeMessage, "utf-8");

            helper.setTo(to);

            String htmlContent;

            if (isOtp) {
                helper.setSubject("Mã xác thực OTP - Bookington");
                htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px; background-color: #ffffff;\">"
                        +
                        "<div style=\"text-align: center; margin-bottom: 20px;\">" +
                        "<h2 style=\"color: #4F46E5; margin: 0;\">Bookington</h2>" +
                        "</div>" +
                        "<p style=\"font-size: 16px; color: #333;\">Xin chào,</p>" +
                        "<p style=\"font-size: 16px; color: #333;\">Mã xác thực (OTP) của bạn là:</p>" +
                        "<div style=\"text-align: center; margin: 30px 0;\">" +
                        "<span style=\"font-size: 32px; font-weight: bold; letter-spacing: 5px; color: #4F46E5; background-color: #EEF2FF; padding: 15px 30px; border-radius: 8px; border: 1px solid #C7D2FE;\">"
                        +
                        content +
                        "</span>" +
                        "</div>" +
                        "<p style=\"font-size: 14px; color: #666;\">Mã này có hiệu lực trong vòng 5 phút. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>"
                        +
                        "<hr style=\"border: none; border-top: 1px solid #eee; margin: 20px 0;\" />" +
                        "<p style=\"font-size: 12px; color: #999; text-align: center;\">© 2024 Bookington. All rights reserved.</p>"
                        +
                        "</div>";
            } else {
                helper.setSubject("Reset mật khẩu thành công");
                htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px; background-color: #ffffff;\">"
                        +
                        "<div style=\"text-align: center; margin-bottom: 20px;\">" +
                        "<h2 style=\"color: #4F46E5; margin: 0;\">Bookington</h2>" +
                        "</div>" +
                        "<p style=\"font-size: 16px; color: #333;\">Xin chào,</p>" +
                        "<p style=\"font-size: 16px; color: #333;\">Mật khẩu của bạn đã được đặt lại thành công.</p>" +
                        "<p style=\"font-size: 16px; color: #333;\">Mật khẩu mới của bạn là:</p>" +
                        "<div style=\"text-align: center; margin: 30px 0;\">" +
                        "<span style=\"font-size: 24px; font-weight: bold; color: #10B981; background-color: #ECFDF5; padding: 15px 30px; border-radius: 8px; border: 1px solid #D1FAE5;\">"
                        +
                        content +
                        "</span>" +
                        "</div>" +
                        "<p style=\"font-size: 14px; color: #666;\">Vui lòng đăng nhập và đổi mật khẩu ngay để đảm bảo an toàn.</p>"
                        +
                        "<hr style=\"border: none; border-top: 1px solid #eee; margin: 20px 0;\" />" +
                        "<p style=\"font-size: 12px; color: #999; text-align: center;\">© 2024 Bookington. All rights reserved.</p>"
                        +
                        "</div>";
            }

            helper.setText(htmlContent, true); // true = isHtml
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("MAIL ERROR", e);
            throw new RuntimeException("Error sending email", e);
        }

    }
}

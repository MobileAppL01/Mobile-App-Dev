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
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            if (isOtp) {
                mailMessage.setSubject("Mã xác thực OTP - Bookington");
                mailMessage.setText("Mã xác thực của bạn là: " + content + "\nMã có hiệu lực trong 5 phút.");
            } else {
                mailMessage.setSubject("Reset mật khẩu thành công");
                mailMessage.setText(
                        "Mật khẩu mới của bạn là:\n" + content + "\n" + "Hãy đổi mật khẩu ngay sau khi đăng nhập");
            }
            mailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("MAIL ERROR", e);
            throw e;
        }

    }
}

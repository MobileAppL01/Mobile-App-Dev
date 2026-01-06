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
    public ResetPasswordService(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
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
        }
        else {
            return ResponseEntity.badRequest().body("Thông tin không hợp lệ");
        }
    }
    public ResponseEntity<String> resetForgotPassword(String email, String phoneNumber) {
        User user = userRepository.findByEmail(email);
        if (user != null&&user.getPhone().equals(phoneNumber)) {
            String newPassword = UUID.randomUUID().toString();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            sendEmail(user.getEmail(), newPassword);
            return ResponseEntity.ok("Mật khẩu mới đã được gửi tới email của bạn");
        }
        else {
            return ResponseEntity.badRequest().body("Thông tin không hợp lệ");
        }



    }
    private void sendEmail(String to, String newPassword) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject("Reset mật khẩu thành công");
            mailMessage.setText("Mật khẩu mới của bạn là:\n" + newPassword+"\n"+"Hãy đổi mật khẩu ngay sau khi đăng nhập");
            mailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("MAIL ERROR", e);
            throw e;
        }

    }
}

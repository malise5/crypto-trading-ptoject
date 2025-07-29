package com.malise.trading.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(String email, String otp) throws MessagingException {
        // Logic to send email using javaMailSender
        // This is a placeholder for the actual implementation
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "utf-8");

        String subject = "Email Verification OTP";
        String content = "<p>Dear User,</p>"
                + "<p>Your OTP for email verification is: <strong>" + otp + "</strong" + "</p> "
                + "<p>Thank you!</p>";
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            // Handle exception, e.g., log it or rethrow it
            throw new MessagingException("Failed to send email", e);
        }
    }
}

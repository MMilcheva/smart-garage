package com.example.smart_garage.services;

import com.example.smart_garage.services.contracts.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Value("${mail.smtp.username}")
    private String fromEmail;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);
    }

    public void sendRegistrationEmail(String to, String username, String password) {
        String subject = "Welcome to our platform!";
        String greeting = "Hello and welcome to our platform! We are excited to have you as a new member.";
        String message = String.format("%s%n%nYour login details are:%nUsername: %s%n%nPlease keep these details safe and do not share them with anyone.", greeting, username, password);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);
    }

    public void sendRegistrationEmailWithoutPassword(String to, String username){
        String subject = "Welcome to our platform!";
        String greeting = "Hello and welcome to our platform! We are excited to have you as a new member.";
        String message = String.format("%s%n%nYour login details are:%nUsername: %s%nPassword: %s%n%nPlease keep these details safe and do not share them with anyone.", greeting, username);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);
    }


    @Override
    public void sendPasswordResetEmail(String toEmail, String resetUrl) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Password reset request");
            helper.setText("Please click the following link to reset your password: " + resetUrl);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendRegistrationAndResetEmail(String toEmail, String username, String resetUrl) {
        String subject = "Welcome to our platform!";
        String greeting = "Hello and welcome to our platform! We are excited to have you as a new member.";
        String message = String.format("%s%n%nYour username is:%nUsername: %s%n%nPlease keep these details safe and do not share them with anyone.%n%nYou have been provided with temporary password, please change it here %s", greeting, username, resetUrl);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(message);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}


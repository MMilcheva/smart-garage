package com.example.smart_garage.services.contracts;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String text);

    void sendPasswordResetEmail(String toEmail, String resetUrl);

    void sendRegistrationEmail(String to, String username, String password);

    void sendRegistrationEmailWithoutPassword(String to, String username);

    void sendRegistrationAndResetEmail(String toEmail, String username, String resetUrl);


}

package com.example.smart_garage.controllers.mvc;


import com.example.smart_garage.models.PasswordResetToken;
import com.example.smart_garage.models.User;
import com.example.smart_garage.services.contracts.EmailSenderService;
import com.example.smart_garage.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.UUID;


@Controller
public class PasswordResetMvcController {
    private UserService userService;
    private EmailSenderService emailSenderService;

    @Autowired
    public PasswordResetMvcController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "ResetPasswordView";
    }

    @PostMapping("/forgot-password")
    public String processForgotPasswordForm(@RequestParam("email") String userEmail, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            // Lookup user by email
            User user = userService.getUserByEmail(userEmail);
            if (user == null) {
                // Display error message
                return "NotFoundView";
            }

            // Generate password reset token
            String token = UUID.randomUUID().toString();

            // Save token to database
            userService.createPasswordResetTokenForUser(user, token);

            // Send password reset email
            String resetUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reset-password?token=" + token;
            emailSenderService.sendPasswordResetEmail(user.getEmail(), resetUrl);

            // Redirect back to forgot-password page with success message
            redirectAttributes.addFlashAttribute("message", "Activation link has been sent to your email");
            return "redirect:/forgot-password?success";
        } catch (Exception e) {
            // Log the error or display an error message to the user
            return "NotFoundView";
        }
    }


    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        try {
            // Lookup user by reset token
            PasswordResetToken resetToken = userService.getPasswordResetToken(token);
            if (resetToken == null) {
                // Display error message
                model.addAttribute("error");
                return "TokenNotFoundView";
            }

            // Check if token has expired
            if (resetToken.isExpired()) {
                // Display error message
                return "TokenNotFoundView";
            }

            // Add token to model and display reset password form
            model.addAttribute("token", token);
            return "ResetLinkView";
        } catch (Exception e) {
            // Handle the exception and redirect the user to an error page
            model.addAttribute("error", "An error occurred while processing your request.");
            return "NotFoundView";
        }
    }



    @PostMapping("/reset-password")
    public String processResetPasswordForm(@RequestParam("token") String token, @RequestParam("password")
    String password, RedirectAttributes redirectAttributes) {

        try {
            // Lookup user by reset token
            PasswordResetToken resetToken = userService.getPasswordResetToken(token);
            if (resetToken == null) {
                // Display error message
                return "NotFoundView2";
            }

            // Check if token has expired
            if (resetToken.isExpired()) {
                // Display error message
                return "NotFoundView2";
            }

            // Set new password for user
            User user = resetToken.getUser();
            userService.changeUserPassword(user, password);

            // Delete password reset token
            userService.deletePasswordResetToken(resetToken);

            // Redirect to login page with success message
            redirectAttributes.addFlashAttribute("message", "Your password has been reset. Please log in with your new password.");
            return "redirect:/auth/login";
        }catch (Exception e){
            // Display error message to the user
            redirectAttributes.addFlashAttribute("error", "An error occurred while resetting your password. Please try again later.");
            return "redirect:/forgot-password";
        }
    }
}







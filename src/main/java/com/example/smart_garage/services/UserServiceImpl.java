package com.example.smart_garage.services;

import com.example.smart_garage.exceptions.*;
import com.example.smart_garage.models.PasswordResetToken;
import com.example.smart_garage.models.User;
import com.example.smart_garage.models.UserFilterOptions;
import com.example.smart_garage.repositories.contracts.DiscountRepository;
import com.example.smart_garage.repositories.contracts.PasswordResetTokenRepository;
import com.example.smart_garage.repositories.contracts.UserRepository;
import com.example.smart_garage.services.contracts.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    public static final String MODIFY_USER_ERROR_MESSAGE = "Only admin or user with the same id can update the information of the user.";
    private final UserRepository userRepository;
    private final DiscountRepository discountRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final JavaMailSender javaMailSender;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, DiscountRepository discountRepository, PasswordResetTokenRepository passwordResetTokenRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.discountRepository = discountRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public List<User> getAll(Optional<String> search) {
        return userRepository.getAll(search);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getByName(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.getUserByPhoneNUmber(phoneNumber);
    }


    @Override
    public List<User> getBlockedUsers() {
        return userRepository.getBlockedUsers();
    }

    @Override
    public User createUser(final User user) {
        boolean duplicateExists = true;

        try {
            userRepository.getByName(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Username", "username", user.getUsername());
        }
        userRepository.create(user);
        return user;
    }

   public void sendRegistrationEmail(User user) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to Smart Garage");
            helper.setText("Dear " + user.getUsername() + ",\n\n" +
                    "Thank you for registering on Smart Garage. Your account has been created with the following credentials:\n\n" +
                    "Username: " + user.getUsername() + "\n" +
                    "Password: " + user.getPassword() + "\n\n" +
                    "Best regards,\n" +
                    "Smart Garage Team");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send registration email", e);
        }
    }

    @Override
    public void update(User user, User userLog) {
        checkModifyPermissions(user.getUserId(), userLog);
        boolean duplicateExists = true;
        try {
            User existingUser = userRepository.getUserByEmail(user.getEmail());
            if (existingUser.getUserId().equals(user.getUserId())) {
                duplicateExists = false;
            }
            if (existingUser.getEmail().equals(user.getEmail()) && existingUser.getUserId().equals(user.getUserId())) {
                duplicateExists = false;
            }

        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }

        userRepository.update(user);
    }

    @Override
    public void delete(Long id, User user) {
        checkModifyPermissions(id, user);
        userRepository.delete(id);
    }

    @Override
    public int getNumberOfUsers() {
        return userRepository.getNumberOfUsers();
    }


    @Override
    public void block(User user) {
        user.setArchived(true);
        userRepository.block(user);
    }

    @Override
    public void unblock(User userToUnBLock) {
        userToUnBLock.setArchived(false);
        userRepository.unblock(userToUnBLock);
    }

    @Override
    public void activate(User user) {
        user.setActivated(true);
        userRepository.activate(user);
    }

    public void deActivate(User user) {
        user.setActivated(true);
        userRepository.activate(user);
    }

    @Override
    public List<User> filterAllUsers(UserFilterOptions filterOptions) {
        return userRepository.filterAllUsers(filterOptions);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public void changeUserPassword(User user, String password) {
        userRepository.updatePassword(user.getUserId(), password);
    }

    @Override
    public void deletePasswordResetToken(PasswordResetToken resetToken) {
        passwordResetTokenRepository.delete(resetToken);
    }

    private void checkModifyPermissions(Long userToUpdateId, User user) {
        User userToUpdate = userRepository.getById(userToUpdateId);
        if (!(user.getRole().getRoleName().equals("admin") || userToUpdate.equals(user))) {
            throw new AuthorizationException(MODIFY_USER_ERROR_MESSAGE);
        }
    }
}

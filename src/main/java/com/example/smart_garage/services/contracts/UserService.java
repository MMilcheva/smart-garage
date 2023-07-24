package com.example.smart_garage.services.contracts;

import com.example.smart_garage.models.PasswordResetToken;
import com.example.smart_garage.models.User;
import com.example.smart_garage.models.UserFilterOptions;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll(Optional<String> search);

    User getUserById(Long id);

    User createUser(User user);

    void update(User user, User userLog);
    void delete(Long id, User user);
    int getNumberOfUsers();

    void sendRegistrationEmail(User user);

    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User getUserByPhoneNumber(String phoneNumber);

    List<User> getBlockedUsers();

    void block(User user);
    void unblock(User userToUnBLock);
    void activate(User user);
    List<User> filterAllUsers(UserFilterOptions filterOptions);
    void createPasswordResetTokenForUser(User user, String token);

    PasswordResetToken getPasswordResetToken(String token);
    void changeUserPassword(User user, String password);
    void deletePasswordResetToken(PasswordResetToken resetToken);

}

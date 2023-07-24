package com.example.smart_garage.helpers;

import com.example.smart_garage.dto.UserResponse;
import com.example.smart_garage.dto.UserSaveRequest;
import com.example.smart_garage.dto.UserUpdateRequest;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.models.Role;
import com.example.smart_garage.models.User;
import com.example.smart_garage.services.contracts.RoleService;
import com.example.smart_garage.services.contracts.UserService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class UserMapper {

    private final UserService userService;
    private final RoleService roleService;

    public UserMapper(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public UserResponse convertToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        Role role = user.getRole();
        role.setRoleName(user.getRole().getRoleName());

        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setConfirmPassword(user.getPassword());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRoleName(role.getRoleName());
        userResponse.setActivated(user.isActivated());
        return userResponse;
    }

    public List<UserResponse> convertToUserResponseList(List<User> users) {
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setUsername(user.getUsername());
            userResponse.setEmail(user.getEmail());
            userResponse.setPassword(user.getPassword());
            userResponse.setConfirmPassword(user.getPassword());
            userResponse.setPhoneNumber(user.getPhoneNumber());
            userResponse.setActivated(user.isActivated());

            Role role = user.getRole();
            if (role != null) {
                userResponse.setRoleName(role.getRoleName());
            }

            userResponses.add(userResponse);
        }
        return userResponses;
    }


    public User convertToUser(UserSaveRequest userSaveRequest) {
        User user = new User();

        user.setFirstName(userSaveRequest.getFirstName());
        user.setLastName(userSaveRequest.getLastName());
        user.setEmail(userSaveRequest.getEmail());
        user.setPhoneNumber(userSaveRequest.getPhoneNumber());
        Role role = roleService.getRoleByName(userSaveRequest.getRoleName());
        user.setRole(role);
        user.setArchived(userSaveRequest.isArchived());
        user.setActivated(userSaveRequest.isActivated());
        return user;
    }

    public User convertToUserToBeUpdated(Long userToBeUpdatedId, UserUpdateRequest userUpdateRequest) {
        User userToBeUpdated = userService.getUserById(userToBeUpdatedId);

        userToBeUpdated.setFirstName(userUpdateRequest.getFirstName());
        userToBeUpdated.setLastName(userUpdateRequest.getLastName());
        userToBeUpdated.setEmail(userUpdateRequest.getEmail());
        userToBeUpdated.setPassword(userUpdateRequest.getPassword());
        userToBeUpdated.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        userToBeUpdated.setRoleName(userUpdateRequest.getRoleName());
        return userToBeUpdated;

    }

    public User createUser(User user) {
        user.setUsername(generateRandomUsername(user.getEmail()));
        user.setPassword(generateRandomPassword());
        if (fieldExists("email", user.getEmail())) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        if (fieldExists("phone_number", user.getPhoneNumber())) {
            throw new DuplicateEntityException("User", "phone_number", user.getPhoneNumber());
        }
        return user;
    }

    public boolean fieldExists(String fieldName, String fieldValue) {
        try {
            if (fieldName.equalsIgnoreCase("email")) {
                userService.getUserByEmail(fieldValue);
            } else if (fieldName.equalsIgnoreCase("phone_number")) {
                userService.getUserByPhoneNumber(fieldValue);
            }
        } catch (EntityNotFoundException e) {
            return false;
        }
        return true;
    }

    public String generateRandomUsername(String email) {
        String username;
        boolean userExists;
        int index = email.indexOf("@");
        String emailPrefix;
        if (index > 0) {
            emailPrefix = email.substring(0, index);
        } else {
            emailPrefix = "";
        }
        int count = 0;
        do {
            if (count > 0) {
                username = emailPrefix + count;
            } else {
                username = emailPrefix;
            }
            try {
                userService.getUserByUsername(username);
                userExists = true;
                count++;
            } catch (EntityNotFoundException e) {
                userExists = false;
            }
        } while (userExists);
        if (username.isEmpty()) {
            Random random = new Random();
            String digits = "0123456789";
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                builder.append(digits.charAt(random.nextInt(digits.length())));
            }
            username = builder.toString();
        }
        return username;
    }
    public String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String specialChars = "~!@#$%^&*()-_+={}[]|\\:;\"'<>,.?/";
        String digits = "0123456789";
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        builder.append(uppercaseLetters.charAt(random.nextInt(uppercaseLetters.length())));
        builder.append(digits.charAt(random.nextInt(digits.length())));
        builder.append(specialChars.charAt(random.nextInt(specialChars.length())));

        for (int i = 3; i < 15; i++) {
            builder.append(chars.charAt(random.nextInt(chars.length())));
        }
        return builder.toString();
    }
}

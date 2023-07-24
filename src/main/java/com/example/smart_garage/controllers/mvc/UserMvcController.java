package com.example.smart_garage.controllers.mvc;

import com.example.smart_garage.dto.UserUpdateRequest;
import com.example.smart_garage.exceptions.AuthenticationFailureException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.UserMapper;
import com.example.smart_garage.models.User;
import com.example.smart_garage.dto.UserFilterDto;
import com.example.smart_garage.models.UserFilterOptions;
import com.example.smart_garage.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@Controller
@RequestMapping("/users")
public class UserMvcController {


    public static final String ERROR_MESSAGE_UPDATE_USER = "You are not authorised!!";
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserMvcController(UserService userService, UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String filterAllUsers(@ModelAttribute("userFilterOptions") UserFilterDto userFilterDto,
                                 Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        UserFilterOptions userFilterOptions = new UserFilterOptions(

                userFilterDto.getFirstName(),
                userFilterDto.getLastName(),
                userFilterDto.getEmail(),
                userFilterDto.getUsername(),
                userFilterDto.getPhoneNumber(),
                userFilterDto.getMinDate(),
                userFilterDto.getMaxDate(),
                userFilterDto.getPlate(),
                userFilterDto.getRoleName(),
                userFilterDto.getVisitId(),
                userFilterDto.getSortBy(),
                userFilterDto.getSortOrder());

        model.addAttribute("users", userService.filterAllUsers(userFilterOptions));
        model.addAttribute("userFilterOptions", userFilterDto);

        return "UsersView";

    }

    @GetMapping("/{userId}")
    public String showSingleUser(@PathVariable Long userId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            user = userService.getUserById(userId);
            model.addAttribute("user", user);
                return "UserView";
          //  }

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";

        }
    }

    @GetMapping("/blocked")
    public String showBlockedUsers(Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            List<User> blockedUsers = userService.getBlockedUsers();
            model.addAttribute("blockedUsers", blockedUsers);
            return "UsersView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{userId}/update")
    public String showUpdateUserPage(@PathVariable Long userId, Model model, HttpSession session) {

        User checkUser;
        try {
            checkUser = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {

            User user = userService.getUserById(userId);
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
            userUpdateRequest.setFirstName(user.getFirstName());
            userUpdateRequest.setLastName(user.getLastName());
            userUpdateRequest.setEmail(user.getEmail());
            userUpdateRequest.setPhoneNumber(user.getPhoneNumber());
            userUpdateRequest.setRoleName(user.getRole().getRoleName());
            userUpdateRequest.setPassword(user.getPassword());
            userUpdateRequest.setCheckPassword(user.getPassword());
            userUpdateRequest.setActivated(user.isActivated());
            model.addAttribute("userId", userId);
            model.addAttribute("userUpdateRequest", userUpdateRequest);
                return "UserUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/{userId}/update")
    public String updateUser(@PathVariable Long userId,
                             @Valid @ModelAttribute("userUpdateRequest") UserUpdateRequest userUpdateRequest,
                             BindingResult bindingResult,
                             Model model, HttpSession session) {
        User checkUser;
        try {
            checkUser = authenticationHelper.tryGetUserWithSession(session);
            checkAccessUpdatePermissions(checkUser, userUpdateRequest.getUserId());
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "UserUpdateView";
        }

        if (!userUpdateRequest.getPassword().equals(userUpdateRequest.getCheckPassword())) {
            bindingResult.rejectValue("passwordConfirm", "password_error",
                    "Password confirmation should match password");
            return "UserUpdateView";
        }
        try {
            User user = userMapper.convertToUserToBeUpdated(userId, userUpdateRequest);
            User userToBeUpdated = userService.getUserById(userId);
            userToBeUpdated.setFirstName(user.getFirstName());
            userToBeUpdated.setPhoneNumber(user.getPhoneNumber());
            userToBeUpdated.setLastName(user.getLastName());
            userToBeUpdated.setEmail(user.getEmail());
            userToBeUpdated.setPassword(user.getPassword());
            if((checkUser.getRole().getRoleName().equals("admin"))){
                userToBeUpdated.setArchived(user.isArchived());
                userToBeUpdated.setRoleName(user.getRole().getRoleName());
            }
            userService.update(userToBeUpdated, checkUser);
            return "redirect:/users/{userId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("email", "duplicate_email", e.getMessage());
            return "UserUpdateView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @DeleteMapping("/{userId}/delete")
    public String delete(@PathVariable int userId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            //TODO fix the method - ne priema nikyde userId!!!
            userService.block(user);
            return "redirect:/users";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    private static void checkAccessPermissions(User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }

    private static void checkAccessUpdatePermissions(User checkedUser, Long userToBeUpdateId) {
        if (!checkedUser.getUserId().equals(userToBeUpdateId)
                && !checkedUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE_UPDATE_USER);
        }
    }


}

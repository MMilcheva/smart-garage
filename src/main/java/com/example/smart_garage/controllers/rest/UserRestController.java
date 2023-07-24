package com.example.smart_garage.controllers.rest;

import com.example.smart_garage.dto.UserResponse;
import com.example.smart_garage.dto.UserSaveRequest;
import com.example.smart_garage.dto.UserUpdateRequest;
import com.example.smart_garage.exceptions.AuthorizationException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.UserMapper;
import com.example.smart_garage.models.User;
import com.example.smart_garage.models.UserFilterOptions;
import com.example.smart_garage.services.contracts.PaymentService;
import com.example.smart_garage.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final PaymentService paymentService;
    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, PaymentService paymentService, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.paymentService = paymentService;
        this.userMapper = userMapper;
    }


    @GetMapping
    public List<User> getAllUsers(@RequestParam(required = false) Optional<String> search) {
        return userService.getAll(search);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        try {
            UserResponse userResponse = userMapper.convertToUserResponse(userService.getUserById(id));
            return ResponseEntity.ok(userResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/blocked")
    public List<User> getBlockedUsers() {
        return userService.getBlockedUsers();
    }

    @GetMapping("/count")
    public ResponseEntity<String> count(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.getRole().getRoleName().equals("admin")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
            }
            int userCount = userService.getNumberOfUsers();
            return new ResponseEntity<>(String.format("%d people are using Java Learning Forum.", userCount), HttpStatus.OK);

        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestHeader HttpHeaders headers,
                                                   @Valid @RequestBody UserSaveRequest userSaveRequest) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user.getUserId(), user);

            user = userMapper.convertToUser(userSaveRequest);
            User modifiedUser = userMapper.createUser(user);
            User savedUser = userService.createUser(modifiedUser);
            userService.sendRegistrationEmail(savedUser);
            UserResponse userResponse = userMapper.convertToUserResponse(savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
            //TODO add Exception Handler with Controller Adviser
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    //TODO create updateAdmin method
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@RequestHeader HttpHeaders headers, @PathVariable Long id,
                                               @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            User authUser = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(authUser.getUserId(), authUser);

            User userToBeUpdated = userMapper.convertToUserToBeUpdated(id, userUpdateRequest);
            userService.update(authUser, userToBeUpdated);
            UserResponse userResponse = userMapper.convertToUserResponse(userToBeUpdated);
            return ResponseEntity.ok(userResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        try {

            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user.getUserId(), user);
            userService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<UserResponse>> filterAllUsers(@RequestHeader HttpHeaders headers,
                                                             @RequestParam(required = false) String firstName,
                                                             @RequestParam(required = false) String lastName,
                                                             @RequestParam(required = false) String email,
                                                             @RequestParam(required = false) String username,
                                                             @RequestParam(required = false) String phoneNumber,
                                                             @RequestParam(required = false) LocalDate minDate,
                                                             @RequestParam(required = false) LocalDate maxDate,
                                                             @RequestParam(required = false) String plate,
                                                             @RequestParam(required = false) String roleDescription,
                                                             @RequestParam(required = false) Integer visitId,
                                                             @RequestParam(required = false) String sortBy,
                                                             @RequestParam(required = false) String sortOrder) {
        //Допълнителен клас за самите параметри Dto + @RequestBody
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user.getUserId(), user);
            UserFilterOptions userFilterOptions = new UserFilterOptions(firstName, lastName, email, username,
                    phoneNumber, minDate, maxDate, plate, roleDescription, visitId, sortBy, sortOrder);
            List<User> filteredUsers = userService.filterAllUsers(userFilterOptions);
            List<UserResponse> userResponses = userMapper.convertToUserResponseList(filteredUsers);
            return ResponseEntity.status(HttpStatus.OK).body(userResponses);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

       private static void checkAccessPermissions(long targetUserId, User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin") || executingUser.getUserId() != targetUserId) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}


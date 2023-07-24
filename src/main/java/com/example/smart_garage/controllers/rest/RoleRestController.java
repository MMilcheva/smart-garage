package com.example.smart_garage.controllers.rest;

import com.example.smart_garage.dto.*;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.RoleMapper;
import com.example.smart_garage.models.Role;
import com.example.smart_garage.models.User;
import com.example.smart_garage.services.contracts.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/roles")
public class RoleRestController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    private final AuthenticationHelper authenticationHelper;

    public RoleRestController(RoleService roleService, RoleMapper roleMapper, AuthenticationHelper authenticationHelper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
        this.authenticationHelper = authenticationHelper;
    }
    @GetMapping
    public List<RoleResponse> getAllRoles(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<String> search) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<Role> roles = roleService.getAllRoles(search);
            return roleMapper.convertToRoleResponses(roles);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponse> getRoleById(@RequestHeader HttpHeaders headers, @PathVariable long roleId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            Role role = roleService.getRoleById(roleId);
            RoleResponse roleResponse = roleMapper.convertToRoleResponse(role);

            return ResponseEntity.ok(roleResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestHeader HttpHeaders headers,
                                                     @Valid @RequestBody RoleSaveRequest roleSaveRequest) {
        try { //TODO user cannot create post if role is blocked
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            Role brand = roleMapper.convertToRole(roleSaveRequest);
            Role savedRole = roleService.createRole(brand);

            RoleResponse brandResponse = roleMapper.convertToRoleResponse(savedRole);
            return ResponseEntity.status(HttpStatus.CREATED).body(brandResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{modelId}")
    public ResponseEntity<RoleResponse> updateRole(@RequestHeader HttpHeaders headers,
                                                     @PathVariable Long roleId,
                                                     @Valid @RequestBody RoleSaveRequest roleSaveRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            Role role = roleMapper.convertToRoleToBeUpdated(roleId, roleSaveRequest);

            Role savedRole = roleService.updateRole(role);

            RoleResponse modelResponse = roleMapper.convertToRoleResponse(savedRole);

            return ResponseEntity.ok(modelResponse);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{roleId}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable long roleId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            roleService.deleteRole(roleId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    private static void checkAccessPermissions(User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}

package com.example.smart_garage.controllers.mvc;

import com.example.smart_garage.dto.RoleResponse;
import com.example.smart_garage.dto.RoleSaveRequest;
import com.example.smart_garage.exceptions.AuthenticationFailureException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.RoleMapper;
import com.example.smart_garage.models.Role;
import com.example.smart_garage.models.User;
import com.example.smart_garage.services.contracts.RoleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;
import static com.example.smart_garage.constants.ExceptionConstant.MODIFY_POST_ERROR_MESSAGE;

@Controller
@RequestMapping("/roles")
public class RoleMvcController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final AuthenticationHelper authenticationHelper;

    public RoleMvcController(RoleService roleService, RoleMapper roleMapper, AuthenticationHelper authenticationHelper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showAllRoles(Model model, HttpSession session) {

        try {
            User user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {

            model.addAttribute("roles", roleService.getRoles());
            return "RolesView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";

        }
    }

    @GetMapping("/{roleId}")
    public String showSingleRole(@PathVariable Long roleId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);

        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Role role = roleService.getRoleById(roleId);
            model.addAttribute("role", role);
            return "RoleView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView2";
        }
    }

    @GetMapping("/new")
    public String showCreateRolePage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("roleSaveRequest", new RoleSaveRequest());
        return "RoleCreateView";
    }

    @PostMapping("/new")
    public String createRole(@Valid @ModelAttribute RoleSaveRequest roleSaveRequest,
                             BindingResult bindingResult,
                             Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "RoleCreateView";
        }

        try {
            Role role = roleMapper.convertToRole(roleSaveRequest);
            Role createdRole = roleService.createRole(role);
            return "redirect:/roles/" + createdRole.getRoleId();

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("title", "duplicate_role", e.getMessage());
            return "RoleCreateView";
        }
    }

    @GetMapping("/{roleId}/update")
    public String showUpdateRolePage(@PathVariable Long roleId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            checkModifyPermissions(roleId, user);
            Role role = roleService.getRoleById(roleId);
            RoleResponse roleResponse = roleMapper.convertToRoleResponse(role);
            RoleSaveRequest roleSaveRequest = new RoleSaveRequest();
            roleSaveRequest.setRoleName(roleResponse.getRoleName());
            List<Role> roles = roleService.getRoles();
            model.addAttribute("roles", roles);
            model.addAttribute("roleId", roleId);
            model.addAttribute("roleSaveRequest", roleSaveRequest);
            return "RoleUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{roleId}/update")
    public String updateRole(@PathVariable Long roleId,
                             @Valid @ModelAttribute("roleSaveRequest") RoleSaveRequest roleSaveRequest,
                             BindingResult bindingResult,
                             Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "RoleUpdateView";
        }
        try {
            checkModifyPermissions(roleId, user);
            Role role = roleMapper.convertToRole(roleSaveRequest);
            Role roleToBeUpdated = roleService.getRoleById(roleId);

            roleToBeUpdated.setRoleName(role.getRoleName());
            roleToBeUpdated.setRoleId(role.getRoleId());
            roleService.updateRole(role);
            return "redirect:/roles/{roleId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("role_name", "duplicate_role_name", e.getMessage());
            return "RoleUpdateView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping("/{roleId}/delete")
    public String delete(@PathVariable Long roleId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            roleService.deleteRole(roleId);
            return "redirect:/roles";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }


    private void checkModifyPermissions(Long roleId, User user) {
        if (!(user.getRole().getRoleName().equals("admin"))) {
            throw new UnauthorizedOperationException(MODIFY_POST_ERROR_MESSAGE);
        }
    }

    private static void checkAccessPermissions(User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}

package com.example.smart_garage.controllers.mvc;

import com.example.smart_garage.dto.*;
import com.example.smart_garage.exceptions.AuthenticationFailureException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.DiscountMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.DiscountService;
import com.example.smart_garage.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@Controller
@RequestMapping("/discounts")
public class DiscountMvcController {


    private final DiscountService discountService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    private final DiscountMapper discountMapper;

    public DiscountMvcController(DiscountService discountService, UserService userService, AuthenticationHelper authenticationHelper, DiscountMapper discountMapper) {
        this.discountService = discountService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.discountMapper = discountMapper;

    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String filterAllDiscounts(@ModelAttribute("discountFilterOptions") DiscountFilterDto discountFilterDto,
                                     Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        String sortBy = discountFilterDto.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "discountName";
        }

        DiscountFilterOptions discountFilterOptions = new DiscountFilterOptions(
                discountFilterDto.getDiscountName(),
                discountFilterDto.getUsername(),
                discountFilterDto.getArchived(),
                discountFilterDto.getSortBy(),
                discountFilterDto.getSortOrder());
        model.addAttribute("canCreateDiscount", true);
        model.addAttribute("discounts", discountService.getAllDiscountsFilter(discountFilterOptions));
        return "DiscountsView";
    }
    @GetMapping("/my-discounts")
    public String filterAllDiscountsByUser(@ModelAttribute("discountFilterOptions") DiscountFilterDto discountFilterDto,
                                     Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        String sortBy = discountFilterDto.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "discountName";
        }
        discountFilterDto.setUsername(user.getUsername());
        DiscountFilterOptions discountFilterOptions = new DiscountFilterOptions(
                discountFilterDto.getDiscountName(),
                discountFilterDto.getUsername(),
                discountFilterDto.getArchived(),
                discountFilterDto.getSortBy(),
                discountFilterDto.getSortOrder());
        model.addAttribute("canCreateDiscount", false);
        model.addAttribute("discounts", discountService.getAllDiscountsFilter(discountFilterOptions));
        return "DiscountsView";
    }
    @GetMapping("/{discountId}")
    public String showSingleDiscount(@PathVariable Long discountId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Discount discount = discountService.getDiscountById(discountId);
            model.addAttribute("discount", discount);
            return "DiscountView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }
    @GetMapping("/new")
    public String showDiscountCreateForm(@ModelAttribute("discountSaveRequest") DiscountSaveRequest discountSaveRequest,
                                         Model model, HttpSession session, @RequestParam(required = false) Optional<String> search, @RequestParam(required = false) Long userId) {
        try {
            User user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);

        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }

        Discount discount = new Discount();
        if (userId != null) {
            User user = userService.getUserById(userId);
            discount.setUser(user);
            discountSaveRequest.setUsername(user.getUsername());
        }

        model.addAttribute("discount", discount);
        model.addAttribute("users", userService.getAll(search));
        model.addAttribute("discountSaveRequest", discountSaveRequest);
        return "DiscountCreateView";
    }

    @PostMapping("/new")
    public String createDiscount(@Valid @ModelAttribute DiscountSaveRequest discountSaveRequest,
                                 BindingResult bindingResult,
                                 Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "DiscountCreateView";
        }

        //TODO - why + createdVisit.getVisitId();
        try {
            Discount discount = discountMapper.convertToDiscount(discountSaveRequest);
            Discount createdDiscount = discountService.createDiscount(discount, user);
            return "redirect:/discounts/" + createdDiscount.getDiscountId();

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";

        }
    }

    @GetMapping("/{discountId}/update")
    public String showUpdateDiscountPage(@PathVariable Long discountId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Discount discount = discountService.getDiscountById(discountId);

            DiscountSaveRequest discountSaveRequest = new DiscountSaveRequest();
            discountSaveRequest.setDiscountName(discount.getDiscountName());
            discountSaveRequest.setDiscountAmount(discount.getDiscountAmount());
            discountSaveRequest.setUserId(discount.getUser().getUserId());
          //  discountSaveRequest.setUsername(discount.getUser().getUsername());
            discountSaveRequest.setValidFrom(discount.getValidFrom());
            discountSaveRequest.setValidTo(discount.getValidTo());

            model.addAttribute("discountSaveRequest", discountSaveRequest);
            return "DiscountUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView2";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }
    //TODO update/discountId ili obratnoto????

    @PostMapping("/{discountId}/update")
    public String updateDiscount(@PathVariable Long discountId,
                                 @Valid @ModelAttribute("discountSaveRequest") DiscountSaveRequest discountSaveRequest,
                                 BindingResult bindingResult,
                                 Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "DiscountUpdateView";
        }
        try {
            //   checkModifyPermissions(roleId,user);
            Discount discountToBeUpdated = discountService.getDiscountById(discountId);
            Discount discount = discountMapper.convertToDiscount(discountSaveRequest);
            discount.setDiscountId(discountId);
            discountService.updateDiscount(discount, user);


            return "redirect:/discounts/{discountId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping("/{discountId}/archive")
    public String delete(@PathVariable Long discountId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Discount discount = discountService.getDiscountById(discountId);
            discountService.archiveDiscount(discount);
            return "redirect:/discounts";
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
}



package com.example.smart_garage.controllers.mvc;

import com.example.smart_garage.dto.*;
import com.example.smart_garage.enumeration.PaymentStatus;
import com.example.smart_garage.exceptions.AuthenticationFailureException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.*;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@Controller
@RequestMapping("/visits")
public class VisitMvcController {
    private final VehicleService vehicleService;
    private final VisitService visitService;
    private final OrderService orderService;
    private final AuthenticationHelper authenticationHelper;
    private final VisitMapper visitMapper;
    private final AuthenticationMapper mapper;

    @Autowired
    public VisitMvcController(VehicleService vehicleService, VisitService visitService,
                              AuthenticationHelper authenticationHelper, VisitMapper visitMapper,
                              AuthenticationMapper mapper, UserService userService, EmailSenderService emailSenderService,
                              UserMapper userMapper, VehicleMapper vehicleMapper, OrderService orderService) {
        this.vehicleService = vehicleService;
        this.visitService = visitService;
        this.authenticationHelper = authenticationHelper;
        this.visitMapper = visitMapper;
        this.mapper = mapper;

        this.orderService = orderService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String filterAllVisits(@ModelAttribute("visitFilterOptions") VisitFilterDto visitFilterDto,
                                  Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        String sortBy = visitFilterDto.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "startDateOfVisit";
        }
        VisitFilterOptions visitFilterOptions = new VisitFilterOptions(
                visitFilterDto.getVisitId(),
                visitFilterDto.getUserId(),
                visitFilterDto.getMinDate(),
                visitFilterDto.getMaxDate(),
                visitFilterDto.getPlate(),
                visitFilterDto.getPaymentStatus(),
                visitFilterDto.getNotes(),
                visitFilterDto.getArchived(),
                visitFilterDto.getSortBy(),
                visitFilterDto.getSortOrder());
        model.addAttribute("canCreateVisit", true);
        model.addAttribute("visits", visitService.getAllVisitsFilter(visitFilterOptions));
        return "VisitsView";
    }

    @GetMapping("/{visitId}")
    public String showSingleVisit(@PathVariable Long visitId, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUserWithSession(session);


        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";

        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }

        VisitResponse visitResponse = visitMapper.convertToVisitResponse(visitService.getVisitById(visitId));
        model.addAttribute("visit", visitResponse);
        return "VisitView";
    }

    @GetMapping("/my-visits")
    public String filterAllVisitsByUser(@ModelAttribute("visitFilterOptions") VisitFilterDto visitFilterDto, Model model,
                                        HttpSession session, BindingResult bindingResult) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        visitFilterDto.setUserId(user.getUserId());
        try {
            VisitFilterOptions visitFilterOptions = new VisitFilterOptions(
                    visitFilterDto.getVisitId(),
                    visitFilterDto.getUserId(),
                    visitFilterDto.getMinDate(),
                    visitFilterDto.getMaxDate(),
                    visitFilterDto.getPlate(),
                    visitFilterDto.getPaymentStatus(),
                    visitFilterDto.getNotes(),
                    visitFilterDto.getArchived(),
                    visitFilterDto.getSortBy(),
                    visitFilterDto.getSortOrder()
            );
            visitFilterOptions.setUserId(Optional.ofNullable(user.getUserId()));
            model.addAttribute("canCreateVisit", false);

            model.addAttribute("visits", visitService.getAllVisitsFilter(visitFilterOptions));
            return "VisitsView";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";

        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping("/new")
    public String showCreateVisitPage(@ModelAttribute("visitSaveRequest") VisitSaveRequest visitSaveRequest, Model model, HttpSession session, @RequestParam(required = false) Optional<String> search, @RequestParam(required = false) Long vehicleId) {
        try {
            User user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);

            Visit visit = new Visit();
            if (vehicleId != null) {
                Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
                visitSaveRequest.setPlate(vehicle.getPlate());
            }

            model.addAttribute("visit", visit);
            model.addAttribute("paymentStatuses", PaymentStatus.values());
            model.addAttribute("vehicles", vehicleService.getAllVehicles(search));
            model.addAttribute("visitSaveRequest", visitSaveRequest);
            return "VisitCreateView";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/new")
    public String createVisit(@ModelAttribute("visitSaveRequest") VisitSaveRequest visitSaveRequest,
                              BindingResult bindingResult,
                              Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
        if (bindingResult.hasErrors()) {
            return "VisitCreateView";
        }
        try {
            Visit visit = visitMapper.convertToVisit(visitSaveRequest);
            Visit createdVisit = visitService.createVisit(visit);
            return "redirect:/visits/" + createdVisit.getVisitId();

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("title", "duplicate_visit", e.getMessage());
            return "VisitCreateView";
        }
    }

    @GetMapping("/{visitId}/update")
    public String showUpdateVisitPage(@PathVariable Long visitId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Visit visit = visitService.getVisitById(visitId);
            VisitResponse visitResponse = visitMapper.convertToVisitResponse(visit);
            VisitSaveRequest visitSaveRequest = new VisitSaveRequest();

            visitSaveRequest.setArchived(visitResponse.isArchived());
            visitSaveRequest.setEndDateOfVisit(visitResponse.getEndDateOfVisit());

            model.addAttribute("visitId", visitId);
            model.addAttribute("visitSaveRequest", visitSaveRequest);
            return "VisitUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{visitId}/update")
    public String updateVisit(@PathVariable Long visitId,
                              @Valid @ModelAttribute("visitSaveRequest") VisitSaveRequest visitSaveRequest,
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
            Visit visit = visitMapper.convertToVisit(visitSaveRequest);

            Visit visitToBeUpdated = visitService.getVisitById(visitId);
            visitToBeUpdated.setArchived(visit.getArchived());
            visitToBeUpdated.setEndDateOfVisit(visit.getEndDateOfVisit());
            visitService.updateVisit(visitToBeUpdated);
            return "redirect:/visits/{visitId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView2";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("title", "duplicate_visit", e.getMessage());
            return "VisitUpdateView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping("/{visitId}/archive")
    public String archive(@PathVariable Long visitId, Model model, HttpSession session, @RequestParam(required = false) Optional<String> search) {
        try {
            User user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
            Visit visit = visitService.getVisitById(visitId);

            visitService.archive(visit, user);
            return "redirect:/visits";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
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



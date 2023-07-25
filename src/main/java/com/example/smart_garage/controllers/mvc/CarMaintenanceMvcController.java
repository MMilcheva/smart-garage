package com.example.smart_garage.controllers.mvc;


import com.example.smart_garage.dto.*;
import com.example.smart_garage.exceptions.*;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.CarMaintenanceMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.CarMaintenanceService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/car-maintenances")
public class CarMaintenanceMvcController {

    private final CarMaintenanceService carMaintenanceService;
    private final AuthenticationHelper authenticationHelper;
    private final CarMaintenanceMapper carMaintenanceMapper;

    public CarMaintenanceMvcController(CarMaintenanceService carMaintenanceService, AuthenticationHelper authenticationHelper, CarMaintenanceMapper carMaintenanceMapper) {
        this.carMaintenanceService = carMaintenanceService;
        this.authenticationHelper = authenticationHelper;
        this.carMaintenanceMapper = carMaintenanceMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/{carMaintenanceId}")
    public String showSingleCarMaintenance(@PathVariable long carMaintenanceId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            CarMaintenance carMaintenance = carMaintenanceService.getCarMaintenanceById(carMaintenanceId);
            model.addAttribute("carMaintenance", carMaintenance);
            return "CarMaintenanceView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView2";
        }
    }

    @GetMapping
    public String filterAllCarMaintenances(@ModelAttribute("carMaintenanceFilterOptions") CarMaintenanceFilterDto carMaintenanceFilterDto,
                                           Model model) {

        String sortBy = carMaintenanceFilterDto.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "carMaintenanceName";
        }

        CarMaintenanceFilterOptions carMaintenanceFilterOptions = new CarMaintenanceFilterOptions(
                carMaintenanceFilterDto.getCarMaintenanceName(),
                carMaintenanceFilterDto.getCarMaintenanceListPrice(),
                carMaintenanceFilterDto.getUsername(),
                carMaintenanceFilterDto.getArchived(),
                carMaintenanceFilterDto.getSortBy(),
                carMaintenanceFilterDto.getSortOrder());
        model.addAttribute("carMaintenances", carMaintenanceService.getAllCarMaintenanceFilter(carMaintenanceFilterOptions));
        return "CarMaintenancesView";
    }
    @GetMapping("/new")
    public String showNewCarMaintenancePage(Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("carMaintenanceSaveRequest", new CarMaintenanceSaveRequest());
        return "CarMaintenanceCreateView";
    }

    @PostMapping("/new")
    public String createCarMaintenance(@Valid @ModelAttribute CarMaintenanceSaveRequest carMaintenanceSaveRequest,
                                       BindingResult bindingResult,
                                       Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "CarMaintenanceCreateView";
        }

        try {
            CarMaintenance carMaintenance = carMaintenanceMapper.convertToCarMaintenance(carMaintenanceSaveRequest);
            CarMaintenance createdCarMaintenance = carMaintenanceService.createCarMaintenance(carMaintenance);
            return "redirect:/car-maintenances/" + createdCarMaintenance.getCarMaintenanceId();

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";

        }
    }

    @GetMapping("/{carMaintenanceId}/update")
    public String showEditCarMaintenancePage(@PathVariable Long carMaintenanceId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {

            CarMaintenance carMaintenance = carMaintenanceService.getCarMaintenanceById(carMaintenanceId);

            CarMaintenanceResponse carMaintenanceResponse = carMaintenanceMapper.convertToCarMaintenanceResponse(carMaintenance);
            CarMaintenanceSaveRequest carMaintenanceSaveRequest = new CarMaintenanceSaveRequest();
            carMaintenanceSaveRequest.setCarMaintenanceName(carMaintenanceResponse.getCarMaintenanceName());
            carMaintenanceSaveRequest.setArchived(carMaintenanceResponse.isArchived());

            model.addAttribute("canManipulateCarMaintenance", true);
            model.addAttribute("carMaintenanceId", carMaintenanceId);
            model.addAttribute("carMaintenanceSaveRequest", carMaintenanceSaveRequest);
            return "CarMaintenanceUpdateView";

        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/{carMaintenanceId}/update")
    public String updateCarMaintenance(@PathVariable Long carMaintenanceId,
                                       @Valid @ModelAttribute("carMaintenanceSaveRequest") CarMaintenanceSaveRequest carMaintenanceSaveRequest,
                                       BindingResult bindingResult,
                                       Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "CarMaintenanceUpdateView";
        }
        try {
            CarMaintenance carMaintenance = carMaintenanceMapper.convertToCarMaintenance(carMaintenanceSaveRequest);
            carMaintenance.setCarMaintenanceName(carMaintenanceSaveRequest.getCarMaintenanceName());
            carMaintenance.setArchived(carMaintenanceSaveRequest.getArchived());
            CarMaintenance carMaintenanceToBeUpdated = carMaintenanceService.getCarMaintenanceById(carMaintenanceId);
            carMaintenanceToBeUpdated.setCarMaintenanceName(carMaintenance.getCarMaintenanceName());
            carMaintenanceToBeUpdated.setArchived(carMaintenance.getArchived());

            carMaintenanceService.updateCarMaintenance(carMaintenanceToBeUpdated);
            return "redirect:/car-maintenances/{carMaintenanceId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView2";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";

        }
    }

}

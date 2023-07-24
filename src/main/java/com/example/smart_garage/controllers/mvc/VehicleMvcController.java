package com.example.smart_garage.controllers.mvc;

import com.example.smart_garage.dto.*;
import com.example.smart_garage.exceptions.AuthenticationFailureException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.VehicleMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.CarModelService;
import com.example.smart_garage.services.contracts.UserService;
import com.example.smart_garage.services.contracts.VehicleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/vehicles")
public class VehicleMvcController {
    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;
    private final UserService userService;
    private final CarModelService carModelService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public VehicleMvcController(VehicleService vehicleService, VehicleMapper vehicleMapper, UserService userService,
                                CarModelService carModelService, AuthenticationHelper authenticationHelper) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
        this.userService = userService;
        this.carModelService = carModelService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("{vehicleId}")
    public String showSingleVehicle(@PathVariable Long vehicleId, Model model, HttpSession session) {

        Vehicle vehicle;
        try {
            vehicle = vehicleService.getVehicleById(vehicleId);
            model.addAttribute("vehicle", vehicle);

            return "VehicleView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";

        } catch (UnauthorizedOperationException e) {
            return "NotFoundView";
        }
    }

    @GetMapping
    public String filterAllVehicles(@ModelAttribute("vehicleFilterOptions") VehicleFilterDto vehicleFilterDto,
                                    Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }


        VehicleFilterOptions vehicleFilterOptions = new VehicleFilterOptions(

                vehicleFilterDto.getVehicleId(),
                vehicleFilterDto.getUserId(),
                vehicleFilterDto.getPlate(),
                vehicleFilterDto.getVin(),
                vehicleFilterDto.getYearOfCreation(),
                vehicleFilterDto.getModelName(),
                vehicleFilterDto.getUsername(),
                vehicleFilterDto.getOwnerFirstName(),
                vehicleFilterDto.getOwnerLastName(),
                vehicleFilterDto.getArchived(),
                vehicleFilterDto.getSortBy(),
                vehicleFilterDto.getSortOrder());
        model.addAttribute("canCreateVehicle", true);
        model.addAttribute("vehicles", vehicleService.getAllVehiclesFilter(vehicleFilterOptions));
        model.addAttribute("vehicleFilterOptions", vehicleFilterDto);

        return "VehiclesView";
    }

    @GetMapping("/my-vehicles")
    public String filterAllVehiclesByUser(@ModelAttribute("vehicleFilterOptions") VehicleFilterDto vehicleFilterDto,
                                          Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        vehicleFilterDto.setUserId(user.getUserId());
        try {
            VehicleFilterOptions vehicleFilterOptions = new VehicleFilterOptions(
                    vehicleFilterDto.getVehicleId(),
                    vehicleFilterDto.getUserId(),
                    vehicleFilterDto.getPlate(),
                    vehicleFilterDto.getVin(),
                    vehicleFilterDto.getYearOfCreation(),
                    vehicleFilterDto.getModelName(),
                    vehicleFilterDto.getUsername(),
                    vehicleFilterDto.getOwnerFirstName(),
                    vehicleFilterDto.getOwnerLastName(),
                    vehicleFilterDto.getArchived(),
                    vehicleFilterDto.getSortBy(),
                    vehicleFilterDto.getSortOrder()
            );
            List<Vehicle> vehicles = vehicleService.getAllVehiclesFilter(vehicleFilterOptions);
            vehicleMapper.convertToVehicleResponses(vehicles);
            model.addAttribute("canCreateVehicle", false);
            model.addAttribute("vehicles", vehicles);
            return "VehiclesView";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @GetMapping("/new")
    public String showCreateVehiclePage(@ModelAttribute("vehicleSaveRequest") VehicleSaveRequest vehicleSaveRequest,
                                        Model model,
                                        HttpSession session,
                                        @RequestParam(required = false) Optional<String> userSearch,
                                        @RequestParam(required = false) Optional<String> carSearch,
                                        @RequestParam(required = false) Long userIdForVehicleCreation) {
        try {
            User user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);

            Vehicle vehicle = new Vehicle();

            if (userIdForVehicleCreation != null) {
                User userForVehicleCreation = userService.getUserById(userIdForVehicleCreation);
                vehicle.setUser(userForVehicleCreation);
                vehicleSaveRequest.setEmail(userForVehicleCreation.getEmail());
            }

            model.addAttribute("vehicle", vehicle);
            model.addAttribute("users", userService.getAll(userSearch));
            model.addAttribute("carModels", carModelService.getAllModels(carSearch));
            model.addAttribute("vehicleSaveRequest", vehicleSaveRequest);


            return "VehicleCreateView";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }


    }

    @PostMapping("/new")
    public String createVehicle(@Valid @ModelAttribute("vehicleSaveRequest") VehicleSaveRequest vehicleSaveRequest,
                                BindingResult bindingResult,
                                Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);

        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            //TODO not authorized view to be created
        }

        if (bindingResult.hasErrors()) {
            return "VehicleCreateView";
        }
        //TODO put userID in SaveRequest
        try {
            Vehicle vehicle = vehicleMapper.convertToVehicle(vehicleSaveRequest);
            Vehicle createdVehicle = vehicleService.createVehicle(vehicle);

            return "redirect:/vehicles/" + createdVehicle.getVehicleId();
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView2";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("title", "duplicate_vehicle", e.getMessage());
            return "VehicleCreateView";
        }
    }


    @GetMapping("/{vehicleId}/update")
    public String showUpdateVehiclePage(@PathVariable Long vehicleId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
            VehicleResponse vehicleResponse = vehicleMapper.convertToVehicleResponse(vehicle);
            VehicleSaveRequest vehicleSaveRequest = new VehicleSaveRequest();

            vehicleSaveRequest.setPlate(vehicleResponse.getPlate());
            vehicleSaveRequest.setEmail(vehicleResponse.getUsername());
            vehicleSaveRequest.setArchived(vehicleResponse.isArchived());
            model.addAttribute("vehicleId", vehicleId);
            model.addAttribute("vehicleSaveRequest", vehicleSaveRequest);
            return "VehicleUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView2";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{vehicleId}/update")
    public String updateVehicle(@PathVariable Long vehicleId,
                                @Valid @ModelAttribute("vehicleSaveRequest") VehicleSaveRequest vehicleSaveRequest,
                                BindingResult bindingResult,
                                Model model, HttpSession session) {
        User checkUser;
        try {
            checkUser = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "VehicleUpdateView";
        }
        try {
            Vehicle vehicle = vehicleMapper.convertToVehicleToBeUpdated(vehicleId, vehicleSaveRequest);
            vehicle.setArchived(vehicleSaveRequest.getArchived());
            vehicle.setPlate(vehicleSaveRequest.getPlate());
            Vehicle vehicleToBeUpdated = vehicleService.getVehicleById(vehicleId);

            vehicleToBeUpdated.setPlate(vehicle.getPlate());
            vehicleToBeUpdated.setArchived(vehicle.isArchived());

            vehicleService.updateVehicle(vehicleToBeUpdated);
            return "redirect:/vehicles/{vehicleId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView2";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("vehicle", "duplicate_vehicle", e.getMessage());
            return "VehicleUpdateView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }


        @GetMapping("/{vehicleId}/delete")
    public String deleteDelete(@PathVariable int vehicleId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            vehicleService.deleteVehicle(vehicleId, user);
            return "redirect:/vehicles";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView2";
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


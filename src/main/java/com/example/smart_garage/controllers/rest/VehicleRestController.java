package com.example.smart_garage.controllers.rest;


import com.example.smart_garage.dto.VehicleResponse;
import com.example.smart_garage.dto.VehicleSaveRequest;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.VehicleMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.CarModelService;
import com.example.smart_garage.services.contracts.UserService;
import com.example.smart_garage.services.contracts.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@RestController
@RequestMapping("/api")
public class VehicleRestController {
    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;
    private final UserService userService;
    private final CarModelService carModelService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public VehicleRestController(VehicleService vehicleService, VehicleMapper vehicleMapper, UserService userService, CarModelService carModelService, AuthenticationHelper authenticationHelper) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
        this.userService = userService;
        this.carModelService = carModelService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("vehicles")
    public List<VehicleResponse> getAll(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<String> search) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<Vehicle> vehicles = vehicleService.getAllVehicles(search);
            return vehicleMapper.convertToVehicleResponses(vehicles);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @GetMapping("vehicles/{vehicleId}")
    public ResponseEntity<VehicleResponse> getVehicleById(@RequestHeader HttpHeaders headers, @PathVariable Long vehicleId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            VehicleResponse vehicleResponse = vehicleMapper.convertToVehicleResponse(vehicleService.getVehicleById(vehicleId));
            return ResponseEntity.ok(vehicleResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @GetMapping("vehicles/filter")
    public List<VehicleResponse> filter(
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) String vin,
            @RequestParam(required = false) Integer yearOfCreation,
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String ownerFirstName,
            @RequestParam(required = false) String ownerLastName,
            @RequestParam(required = false) Boolean isArchived,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        VehicleFilterOptions vehicleFilterOptions = new VehicleFilterOptions(vehicleId, userId, plate, vin, yearOfCreation,
                modelName,username, ownerFirstName, ownerLastName, isArchived, sortBy, sortOrder);
        try {
            List<Vehicle> vehicles = vehicleService.getAllVehiclesFilter(vehicleFilterOptions);
            return vehicleMapper.convertToVehicleResponses(vehicles);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("vehicles/{vehicleId}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable long vehicleId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            vehicleService.deleteVehicle(vehicleId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("users/{userId}")
    public ResponseEntity<VehicleResponse> createVehicle(@RequestHeader HttpHeaders headers,
                                                         @PathVariable Long userId,
                                                         @RequestParam Long modelId,
                                                         @Valid @RequestBody VehicleSaveRequest vehicleSaveRequest) {

        try {
            User authUser = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(authUser);
            User user = userService.getUserById(userId);
            CarModel model = carModelService.getCarModelById(modelId);
            Vehicle vehicle = vehicleMapper.convertToVehicle(vehicleSaveRequest);
            vehicle.setModel(model);
            vehicle.setUser(user);
            Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

            VehicleResponse vehicleResponse = vehicleMapper.convertToVehicleResponse(savedVehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(vehicleResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @PutMapping("vehicles/{vehicleId}")
    public ResponseEntity<VehicleResponse> updateVehicle(@RequestHeader HttpHeaders headers,
                                                         @PathVariable Long vehicleId,
                                                         @Valid @RequestBody VehicleSaveRequest vehicleSaveRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            Vehicle vehicle = vehicleMapper.convertToVehicleToBeUpdated(vehicleId, vehicleSaveRequest);

            Vehicle savedVehicle = vehicleService.updateVehicle(vehicle);

            VehicleResponse vehicleResponse = vehicleMapper.convertToVehicleResponse(savedVehicle);

            return ResponseEntity.ok(vehicleResponse);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @GetMapping("/users/{userId}/vehicles")
    public List<VehicleResponse> getAllOrdersByUserId(@RequestHeader HttpHeaders headers, @PathVariable Long userId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<Vehicle> vehicles = vehicleService.getAllVehiclesByUserId(userId);
            return vehicleMapper.convertToVehicleResponses(vehicles);
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

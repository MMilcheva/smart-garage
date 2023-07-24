package com.example.smart_garage.controllers.rest;


import com.example.smart_garage.dto.CarMaintenanceResponse;
import com.example.smart_garage.dto.CarMaintenanceSaveRequest;
import com.example.smart_garage.exceptions.AuthorizationException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.CarMaintenanceMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.models.CarMaintenance;
import com.example.smart_garage.services.contracts.CarMaintenanceService;
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
@RequestMapping("/api/car-maintenances")
public class CarMaintenanceRestController {

     private final CarMaintenanceService carMaintenanceService;

    private final AuthenticationHelper authenticationHelper;

    private final CarMaintenanceMapper carMaintenanceMapper;

    public CarMaintenanceRestController(CarMaintenanceService carMaintenanceService, AuthenticationHelper authenticationHelper, CarMaintenanceMapper carMaintenanceMapper) {
        this.carMaintenanceService = carMaintenanceService;
        this.authenticationHelper = authenticationHelper;
        this.carMaintenanceMapper = carMaintenanceMapper;
    }

    @GetMapping
    public List<CarMaintenanceResponse> getAllCarMaintenances(@RequestHeader HttpHeaders headers,
                                                              @RequestParam(required = false) Optional<String> search) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<CarMaintenance> carMaintenances = carMaintenanceService.getAllCarMaintenances(search);
            return carMaintenanceMapper.convertToCarMaintenanceResponses(carMaintenances);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{carMaintenanceId}")
    public ResponseEntity<CarMaintenanceResponse> getCarMaintenanceById(@RequestHeader HttpHeaders headers, @PathVariable long carMaintenanceId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);

            checkAccessPermissions(user);

            CarMaintenanceResponse carMaintenanceResponse = carMaintenanceMapper.convertToCarMaintenanceResponse
                    (carMaintenanceService.getCarMaintenanceById(carMaintenanceId));
            return ResponseEntity.ok(carMaintenanceResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/my-car-maintenances")
    public List<CarMaintenance> getAllCarMaintenancesByUsername(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return carMaintenanceService.getAllCarMaintenancesByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }
    @GetMapping("/filter")
    public List<CarMaintenanceResponse> filter(
             @RequestParam(required = false) String carMaintenanceName,
             @RequestParam(required = false) Double carMaintenancePrice,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Boolean isArchived,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {

        CarMaintenanceFilterOptions carMaintenanceFilterOptions = new CarMaintenanceFilterOptions(carMaintenanceName, carMaintenancePrice, username, isArchived, sortBy, sortOrder);


        try {
            List<CarMaintenance> carMaintenances = carMaintenanceService.getAllCarMaintenanceFilter(carMaintenanceFilterOptions);
            return carMaintenanceMapper.convertToCarMaintenanceResponses(carMaintenances);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping
    ResponseEntity<CarMaintenanceResponse> createCarMaintenance(@RequestHeader HttpHeaders headers,
                                                        @Valid @RequestBody CarMaintenanceSaveRequest carMaintenanceSaveRequest) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

        CarMaintenance carMaintenance = carMaintenanceMapper.convertToCarMaintenance(carMaintenanceSaveRequest);
        CarMaintenance saverMaintenance = carMaintenanceService.createCarMaintenance(carMaintenance);
        CarMaintenanceResponse carMaintenanceResponse = carMaintenanceMapper.convertToCarMaintenanceResponse(saverMaintenance);
            return ResponseEntity.status(HttpStatus.CREATED).body(carMaintenanceResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{carMaintenanceId}")
    public ResponseEntity<CarMaintenanceResponse> updateCarMaintenance(@RequestHeader HttpHeaders headers, @PathVariable Long carMaintenanceId,
                                                     @Valid @RequestBody CarMaintenanceSaveRequest carMaintenanceSaveRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            CarMaintenance carMaintenance = carMaintenanceMapper.convertToCarMaintenanceToBeUpdated(carMaintenanceId,carMaintenanceSaveRequest);
            CarMaintenance savedMaintenance = carMaintenanceService.updateCarMaintenance(carMaintenance);
            CarMaintenanceResponse carMaintenanceResponse = carMaintenanceMapper.convertToCarMaintenanceResponse(savedMaintenance);
            return ResponseEntity.status(HttpStatus.CREATED).body(carMaintenanceResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            carMaintenanceService.deleteCarMaintenance(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private static void checkAccessPermissions(User executingUser) {

        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }else if (executingUser.isArchived()){
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}

package com.example.smart_garage.controllers.rest;

import com.example.smart_garage.dto.*;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.VisitMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.VehicleService;
import com.example.smart_garage.services.contracts.VisitService;
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
@RequestMapping("/api/visits")
public class VisitRestController {
    private final VehicleService vehicleService;
    private final VisitService visitService;
    private final AuthenticationHelper authenticationHelper;
    private final VisitMapper visitMapper;
    @Autowired
    public VisitRestController(VehicleService vehicleService, VisitService visitService, AuthenticationHelper authenticationHelper, VisitMapper visitMapper) {
        this.vehicleService = vehicleService;
        this.visitService = visitService;
        this.authenticationHelper = authenticationHelper;
        this.visitMapper = visitMapper;
    }

    @GetMapping
    List<VisitResponse> getAllVisits(@RequestHeader HttpHeaders headers) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<Visit> visits = visitService.getAllVisits();
            return visitMapper.convertToVisitResponses(visits);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<VisitResponse> createVisit(@RequestHeader HttpHeaders headers, @Valid @RequestBody VisitSaveRequest visitSaveRequest) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            Vehicle vehicle = vehicleService.getVehicleByPlate(visitSaveRequest.getPlate());
            User owner = vehicle.getUser();
            checkAccessPermissions(user, owner);

            Visit visit = visitMapper.convertToVisit(visitSaveRequest);

            Visit savedVisit = visitService.createVisit(visit);

            VisitResponse visitResponse = visitMapper.convertToVisitResponse(savedVisit);
            return ResponseEntity.status(HttpStatus.CREATED).body(visitResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{visitId}")
    public ResponseEntity<VisitResponse> getVisitById(@RequestHeader HttpHeaders headers, @PathVariable Long visitId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Vehicle vehicle = vehicleService.getVehicleById(visitId);
            User owner = vehicle.getUser();
            checkAccessPermissions(user, owner);

            VisitResponse visitResponse = visitMapper.convertToVisitResponse(visitService.getVisitById(visitId));
            return ResponseEntity.ok(visitResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("user/{userId}")
    public List<VisitResponse>  getAllVisitsByUserId(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);

            List<Visit> visits = visitService.getAllVisitsByUserId(user.getUserId());
            return visitMapper.convertToVisitResponses(visits);
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
    private static void checkAccessPermissions(User executingUser, User owner) {
        if (executingUser.getUserId()!=owner.getUserId()&&!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}

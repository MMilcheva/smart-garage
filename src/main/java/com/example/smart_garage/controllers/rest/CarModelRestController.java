package com.example.smart_garage.controllers.rest;

import com.example.smart_garage.dto.CarModelResponse;
import com.example.smart_garage.dto.CarModelSaveRequest;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.CarModelMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.CarModelService;
import com.example.smart_garage.services.contracts.BrandService;
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
@RequestMapping("/api/car-models")
public class CarModelRestController {
    private final CarModelService carModelService;
    private final BrandService brandService;
    private final CarModelMapper carModelMapper;
    private final AuthenticationHelper authenticationHelper;


    public CarModelRestController(CarModelService carModelService, BrandService brandService, CarModelMapper carModelMapper, AuthenticationHelper authenticationHelper) {
        this.carModelService = carModelService;
        this.brandService = brandService;
        this.carModelMapper = carModelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<CarModelResponse> getAll(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<String> search) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<CarModel> models = carModelService.getAllModels(search);
            return carModelMapper.convertToCarModelResponses(models);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{carModelId}")
    public ResponseEntity<CarModelResponse> getModelById(@RequestHeader HttpHeaders headers, @PathVariable long carModelId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);

            checkAccessPermissions(user);

            CarModelResponse carModelResponse = carModelMapper.convertToCarModelResponse(carModelService.getCarModelById(carModelId));
            return ResponseEntity.ok(carModelResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<CarModelResponse> filter(
            @RequestParam(required = false) Long carModelId,
            @RequestParam(required = false) String carModelName,
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) Boolean isArchived,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        CarModelFilterOptions carModelFilterOptions = new CarModelFilterOptions(carModelId, carModelName, brandName, isArchived, sortBy, sortOrder);
        try {
            List<CarModel> models = carModelService.getAllCarModelsFilter(carModelFilterOptions);
            return carModelMapper.convertToCarModelResponses(models);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("brands/{brandId}")
    public ResponseEntity<CarModelResponse> createModel(@RequestHeader HttpHeaders headers,
                                                        @PathVariable Long brandId,
                                                        @Valid @RequestBody CarModelSaveRequest carModelSaveRequest) {
        try { //TODO user cannot create post if role is blocked
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            Brand brand = brandService.getBrandById(brandId);
            CarModel carModel = carModelMapper.convertToCarModel(carModelSaveRequest);
            carModel.setBrand(brand);
            CarModel savedModel = carModelService.createCarModel(carModel);

            CarModelResponse carModelResponse = carModelMapper.convertToCarModelResponse(savedModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(carModelResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{carModelId}")
    public ResponseEntity<CarModelResponse> updateModel(@RequestHeader HttpHeaders headers,
                                                        @PathVariable Long carModelId,
                                                        @Valid @RequestBody CarModelSaveRequest carModelSaveRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            CarModel carModel = carModelMapper.convertToCarModel(carModelSaveRequest);
            carModel.setCarModelId(carModelId);
            CarModel savedModel = carModelService.updateCarModel(carModel);

            CarModelResponse carModelResponse = carModelMapper.convertToCarModelResponse(savedModel);

            return ResponseEntity.ok(carModelResponse);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{carModelId}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable long carModelId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            carModelService.deleteCarModel(carModelId);
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




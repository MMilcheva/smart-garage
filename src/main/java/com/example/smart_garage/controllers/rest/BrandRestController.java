package com.example.smart_garage.controllers.rest;

import com.example.smart_garage.dto.BrandResponse;
import com.example.smart_garage.dto.BrandSaveRequest;
import com.example.smart_garage.dto.CarModelResponse;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.BrandMapper;
import com.example.smart_garage.helpers.CarModelMapper;
import com.example.smart_garage.models.Brand;
import com.example.smart_garage.models.BrandFilterOptions;
import com.example.smart_garage.models.CarModel;
import com.example.smart_garage.models.User;
import com.example.smart_garage.services.contracts.BrandService;
import com.example.smart_garage.services.contracts.CarModelService;
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
@RequestMapping("/api/brands")
public class BrandRestController {

    private final BrandService brandService;
    private final BrandMapper brandMapper;
    private final CarModelMapper carModelMapper;
    private final CarModelService carModelService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public BrandRestController(BrandService brandService, BrandMapper brandMapper, CarModelMapper carModelMapper, CarModelService carModelService, AuthenticationHelper authenticationHelper) {
        this.brandService = brandService;
        this.brandMapper = brandMapper;
        this.carModelMapper = carModelMapper;
        this.carModelService = carModelService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<BrandResponse> getAllBrands(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<String> search) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<Brand> brands = brandService.getAllBrands(search);
            return brandMapper.convertToBrandResponses(brands);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<BrandResponse> getBrandById(@RequestHeader HttpHeaders headers, @PathVariable Long brandId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);

            checkAccessPermissions(user);

            BrandResponse brandResponse = brandMapper.convertToBrandResponse(brandService.getBrandById(brandId));
            return ResponseEntity.ok(brandResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @GetMapping("/{brandId}/car-models")
    public List<CarModelResponse> getAllCarModelsByBrandId(@RequestHeader HttpHeaders headers, @PathVariable Long brandId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<CarModel> models = carModelService.getAllCarModelsByBrandId(brandId);
            return carModelMapper.convertToCarModelResponses(models);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @GetMapping("/filter")
    public List<BrandResponse> filter(
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) Boolean isArchived,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {

        BrandFilterOptions brandFilterOptions = new BrandFilterOptions(brandName, isArchived, sortBy, sortOrder);


        try {
            List<Brand> brands = brandService.getAllBrandsFilter(brandFilterOptions);
            return brandMapper.convertToBrandResponses(brands);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{brandId}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable long brandId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            brandService.deleteBrand(brandId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(@RequestHeader HttpHeaders headers,
                                                     @Valid @RequestBody BrandSaveRequest brandSaveRequest) {
        try { //TODO user cannot create post if role is blocked
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            Brand brand = brandMapper.convertToBrand(brandSaveRequest);
            Brand savedBrand = brandService.createBrand(brand);

            BrandResponse brandResponse = brandMapper.convertToBrandResponse(savedBrand);
            return ResponseEntity.status(HttpStatus.CREATED).body(brandResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{brandId}")
    public ResponseEntity<BrandResponse> updateBrand(@RequestHeader HttpHeaders headers,
                                                     @PathVariable Long brandId,
                                                     @Valid @RequestBody BrandSaveRequest brandSaveRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            Brand brand = brandMapper.convertToBrandToBeUpdated(brandId, brandSaveRequest);

            Brand savedBrand = brandService.updateBrand(brand);

            BrandResponse brandResponse = brandMapper.convertToBrandResponse(savedBrand);

            return ResponseEntity.ok(brandResponse);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
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

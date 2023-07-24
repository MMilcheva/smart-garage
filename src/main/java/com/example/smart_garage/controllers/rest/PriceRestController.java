package com.example.smart_garage.controllers.rest;

import com.example.smart_garage.dto.*;
import com.example.smart_garage.exceptions.AuthorizationException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.PriceMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.CarMaintenanceService;
import com.example.smart_garage.services.contracts.PriceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/prices")
public class PriceRestController {

    private final PriceService priceService;
    private final CarMaintenanceService carMaintenanceService;
    private final AuthenticationHelper authenticationHelper;
    private final PriceMapper priceMapper;

    public PriceRestController(PriceService priceService, CarMaintenanceService carMaintenanceService, AuthenticationHelper authenticationHelper, PriceMapper priceMapper) {
        this.priceService = priceService;
        this.carMaintenanceService = carMaintenanceService;
        this.authenticationHelper = authenticationHelper;
        this.priceMapper = priceMapper;
    }

    @GetMapping
    public List<PriceResponse> getAllPrices(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<String> search) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<Price> prices = priceService.getAllPrices(search);
            return priceMapper.convertToPriceResponses(prices);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{priceId}")
    public ResponseEntity<PriceResponse> getPriceById(@RequestHeader HttpHeaders headers, @PathVariable Long priceId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            PriceResponse priceResponse = priceMapper.convertToPriceResponse(priceService.getPriceById(priceId));
            return ResponseEntity.ok(priceResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<PriceResponse> filter(
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) LocalDate validOn,
            @RequestParam(required = false) String carMaintenanceName,
            @RequestParam(required = false) Long carMaintenanceId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        PriceFilterOptions priceFilterOptions = new PriceFilterOptions(amount, validOn, carMaintenanceName,
                carMaintenanceId, sortBy, sortOrder);
        try {
            List<Price> price = priceService.getAllPricesFilter(priceFilterOptions);
            return priceMapper.convertToPriceResponse(price);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<PriceResponse> createPrice(@RequestHeader HttpHeaders headers,
                                                     @Valid @RequestBody PriceSaveRequest priceSaveRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            Price price = priceMapper.convertToPrice(priceSaveRequest);
            Price savedPrice = priceService.createPrice(price, user);
            PriceResponse priceResponse = priceMapper.convertToPriceResponse(savedPrice);
            return ResponseEntity.status(HttpStatus.CREATED).body(priceResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public Price updatePrice(@RequestHeader HttpHeaders headers, @PathVariable Long id,
                               @Valid @RequestBody Price price) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            price.setPriceId(id);
            priceService.updatePrice(price, user);
            return price;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deletePrice(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            priceService.deletePrice(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private static void checkAccessPermissions(User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }

}





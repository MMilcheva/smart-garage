package com.example.smart_garage.controllers.rest;

import com.example.smart_garage.dto.BrandResponse;
import com.example.smart_garage.dto.DiscountResponse;
import com.example.smart_garage.dto.DiscountSaveRequest;
import com.example.smart_garage.dto.PriceResponse;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.DiscountMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.DiscountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/discounts")
public class DiscountRestController {

    private final DiscountService discountService;

    private final AuthenticationHelper authenticationHelper;

    private final DiscountMapper discountMapper;

    @Autowired
    public DiscountRestController(DiscountService discountService, AuthenticationHelper authenticationHelper,
                                  DiscountMapper discountMapper) {
        this.discountService = discountService;
        this.authenticationHelper = authenticationHelper;
        this.discountMapper = discountMapper;
    }


    @GetMapping
    public List<DiscountResponse> getAll(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<String> search) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<Discount> discounts = discountService.getAllDiscounts(search);
            return discountMapper.convertToDiscountResponses(discounts);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @GetMapping("/filter")
    public List<DiscountResponse> filter(
            @RequestParam(required = false) String discountName,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Boolean isArchived,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {

        DiscountFilterOptions discountFilterOptions = new DiscountFilterOptions(discountName, username, isArchived, sortBy, sortOrder);


        try {
            List<Discount> discounts = discountService.getAllDiscountsFilter(discountFilterOptions);
            return discountMapper.convertToDiscountResponses(discounts);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{discountId}")
    public ResponseEntity<DiscountResponse> getDiscountById(@RequestHeader HttpHeaders headers, @PathVariable Long discountId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            DiscountResponse discountResponse = discountMapper.convertToDiscountResponse(discountService.getDiscountById(discountId));
            return ResponseEntity.ok(discountResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/users/{userId}")//TODO to check
    public List<Discount> getAllDiscountsByUserId(@PathVariable Long userId) {
        return discountService.getAllDiscountsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<DiscountResponse> createDiscount(@RequestHeader HttpHeaders headers,
                                                           @Valid @RequestBody DiscountSaveRequest discountSaveRequest) {
        try { //TODO user cannot create post if role is blocked
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            Discount discount = discountMapper.convertToDiscount(discountSaveRequest);

            Discount savedDiscount = discountService.createDiscount(discount, user);

            DiscountResponse discountResponse = discountMapper.convertToDiscountResponse(savedDiscount);
            return ResponseEntity.status(HttpStatus.CREATED).body(discountResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{discountId}")
    public ResponseEntity<DiscountResponse> updateDiscount(@RequestHeader HttpHeaders headers,
                                                           @PathVariable Long discountId,
                                                           @Valid @RequestBody DiscountSaveRequest discountSaveRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            Discount discount = discountMapper.convertToDiscount(discountSaveRequest);
            discount.setDiscountId(discountId);
            Discount saveDiscount = discountService.updateDiscount(discount, user);
            DiscountResponse discountResponse = discountMapper.convertToDiscountResponse(saveDiscount);

            return ResponseEntity.ok(discountResponse);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{discountId}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable long discountId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            discountService.deleteDiscount(discountId, user);
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

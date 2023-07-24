package com.example.smart_garage.controllers.rest;

import com.example.smart_garage.dto.PaymentResponse;
import com.example.smart_garage.dto.PaymentSaveRequest;
import com.example.smart_garage.enumeration.Currency;
import com.example.smart_garage.enumeration.PaymentStatus;
import com.example.smart_garage.exceptions.*;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.PaymentMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.CurrencyConversionService;
import com.example.smart_garage.services.contracts.PaymentService;
import com.example.smart_garage.services.contracts.VisitService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/payments")
public class PaymentRestController {

    private final PaymentService paymentService;
    private final AuthenticationHelper authenticationHelper;
    private final PaymentMapper paymentMapper;
    private final CurrencyConversionService currencyConversionService;
private final VisitService visitService;

    @Autowired
    public PaymentRestController(PaymentService paymentService, AuthenticationHelper authenticationHelper, PaymentMapper paymentMapper, CurrencyConversionService currencyConversionService, VisitService visitService) {
        this.paymentService = paymentService;
        this.authenticationHelper = authenticationHelper;
        this.paymentMapper = paymentMapper;
        this.currencyConversionService = currencyConversionService;
        this.visitService = visitService;
    }

    //TODO implement Discount

    @GetMapping
    public List<PaymentResponse> getAllPayments(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<String> search) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<Payment> payments = paymentService.getAllPayments(search);
            return paymentMapper.convertToPaymentResponses(payments);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user.getUserId(), user);
            PaymentResponse paymentResponse = paymentMapper.convertToPaymentResponse(paymentService.getPaymentById(id));
            return ResponseEntity.ok(paymentResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestHeader HttpHeaders headers, @RequestBody PaymentSaveRequest paymentSaveRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user.getUserId(), user);


            Payment payment = paymentMapper.convertToPayment(paymentSaveRequest);
            Visit visit = visitService.getVisitById(paymentSaveRequest.getVisitId());

            Double exchangeRate = currencyConversionService.getExchangerateOnDate(visit.getStartDateOfVisit(), paymentSaveRequest.getSelectedCurrency().getValue, "BGN");


            Payment savedPayment = paymentService.createPayment(visit.getVisitId(), paymentSaveRequest.getSelectedCurrency(), exchangeRate);
            PaymentResponse paymentResponse = paymentMapper.convertToPaymentResponse(savedPayment);
//
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{paymentId}/book-payment")
    public Payment bookPayment(@RequestHeader HttpHeaders headers, @PathVariable Long paymentId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            return paymentService.bookPayment(paymentId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/payments/{paymentId}")
    public void deletePayment(@PathVariable Long paymentId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            paymentService.deletePayment(paymentId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/payments/filter")
    public List<Payment> filterAllPayments(@RequestHeader HttpHeaders headers,
                                           @RequestParam(required = false) Long userId,
                                           @RequestParam(required = false) Double listPrice,
                                           @RequestParam(required = false) Double discount,
                                           @RequestParam(required = false) Double vat,
                                           @RequestParam(required = false) Double totalPriceBGN,
                                           @RequestParam(required = false) LocalDate dateOfPayment,
                                           @RequestParam(required = false) PaymentStatus paymentStatus,
                                           @RequestParam(required = false) Currency selectedCurrency,
                                           @RequestParam(required = false) Double exchangeRate,
                                           @RequestParam(required = false) Boolean isArchived,
                                           @RequestParam(required = false) Long visitId,
                                           @RequestParam(required = false) String sortBy,
                                           @RequestParam(required = false) String sortOrder) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user.getUserId(), user);

            PaymentFilterOptions paymentFilterOptions = new PaymentFilterOptions(userId, listPrice,  discount,  vat,  totalPriceBGN,
                    dateOfPayment, paymentStatus, selectedCurrency,  exchangeRate,  isArchived, visitId, sortBy,sortOrder);
            return paymentService.filterAllPayments(paymentFilterOptions);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{paymentId}/delete")
    public String delete(@PathVariable Long paymentId, HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            checkAccessPermissions(user);
            paymentService.deletePayment(paymentId, user);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";

        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";

        }
        return "redirect:/payments";
    }
    private static void checkAccessPermissions(User user) {
        if (!user.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }

    private static void checkAccessPermissions(long targetUserId, User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin") || executingUser.getUserId() != targetUserId) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}

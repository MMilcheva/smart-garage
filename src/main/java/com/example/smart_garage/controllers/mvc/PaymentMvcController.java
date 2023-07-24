package com.example.smart_garage.controllers.mvc;

import com.example.smart_garage.dto.*;
import com.example.smart_garage.enumeration.Currency;
import com.example.smart_garage.exceptions.AuthenticationFailureException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.PaymentMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.PaymentService;
import com.example.smart_garage.services.contracts.VisitService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@Controller
@RequestMapping("/payments")
public class PaymentMvcController {


    private final PaymentService paymentService;
    private final AuthenticationHelper authenticationHelper;
    private final PaymentMapper paymentMapper;
    private final VisitService visitService;

    public PaymentMvcController(PaymentService paymentService, AuthenticationHelper authenticationHelper, PaymentMapper paymentMapper, VisitService visitService) {
        this.paymentService = paymentService;
        this.authenticationHelper = authenticationHelper;
        this.paymentMapper = paymentMapper;
        this.visitService = visitService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String filterAllPayments(@ModelAttribute("paymentFilterOptions") PaymentFilterDto paymentFilterDto, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        PaymentFilterOptions paymentFilterOptions = new PaymentFilterOptions(
                paymentFilterDto.getUserId(),
                paymentFilterDto.getListPrice(), paymentFilterDto.getDiscount(), paymentFilterDto.getVat(),
                paymentFilterDto.getTotalPriceBGN(), paymentFilterDto.getDateOfPayment(),
                paymentFilterDto.getPaymentStatus(), paymentFilterDto.getSelectedCurrency(),
                paymentFilterDto.getExchangeRate(), paymentFilterDto.getArchived(),
                paymentFilterDto.getVisitId(), paymentFilterDto.getSortBy(), paymentFilterDto.getSortOrder());

        List<PaymentResponse> paymentResponses = paymentMapper.convertToPaymentResponses(paymentService.filterAllPayments(paymentFilterOptions));
        model.addAttribute("canCreatePayment", true);
        model.addAttribute("currencyCodes", Currency.values());
        model.addAttribute("paymentResponses", paymentResponses);
        model.addAttribute("paymentFilterOptions", paymentFilterDto);

        return "PaymentsView";
    }
    @GetMapping("/my-payments")
    public String filterAllPaymentsByUser(@ModelAttribute("paymentFilterOptions") PaymentFilterDto paymentFilterDto, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        paymentFilterDto.setUserId(user.getUserId());
        PaymentFilterOptions paymentFilterOptions = new PaymentFilterOptions(
                paymentFilterDto.getUserId(),
                paymentFilterDto.getListPrice(), paymentFilterDto.getDiscount(), paymentFilterDto.getVat(),
                paymentFilterDto.getTotalPriceBGN(), paymentFilterDto.getDateOfPayment(),
                paymentFilterDto.getPaymentStatus(), paymentFilterDto.getSelectedCurrency(),
                paymentFilterDto.getExchangeRate(), paymentFilterDto.getArchived(),
                paymentFilterDto.getVisitId(), paymentFilterDto.getSortBy(), paymentFilterDto.getSortOrder());

        List<PaymentResponse> paymentResponses = paymentMapper.convertToPaymentResponses(paymentService.filterAllPayments(paymentFilterOptions));
        model.addAttribute("canCreatePayment", false);
        model.addAttribute("currencyCodes", Currency.values());
        model.addAttribute("paymentResponses", paymentResponses);
        model.addAttribute("paymentFilterOptions", paymentFilterDto);

        return "PaymentsView";
    }

    @GetMapping("/{paymentId}")
    public String showSinglePayment(@PathVariable Long paymentId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            PaymentResponse paymentResponse = paymentMapper.convertToPaymentResponse(payment);

            model.addAttribute("paymentResponse", paymentResponse);
            return "PaymentView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }


    @GetMapping("/new")
    public String showCreatePaymentPage(@ModelAttribute("paymentSaveRequest") PaymentSaveRequest paymentSaveRequest, Model model, HttpSession session, @RequestParam(required = false) Long visitId) {
        try {
            User user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);

            Payment payment = new Payment();

            if (visitId != null) {
                Visit visit = visitService.getVisitById(visitId);
                payment.setVisit(visit);
            }
            model.addAttribute("payment", payment);
            model.addAttribute("visits", visitService.getAllVisits());
            model.addAttribute("currencyCodes", Currency.values());
            model.addAttribute("paymentSaveRequest", paymentSaveRequest);
            return "PaymentCreateView";


        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";

        }


    }

    @PostMapping("/new")
    public String createPayment(@Valid @ModelAttribute("paymentSaveRequest") PaymentSaveRequest paymentSaveRequest, BindingResult bindingResult, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "PaymentCreateView";
        }

        //TODO - why + createdVisit.getVisitId();
        try {
            Payment payment = paymentMapper.convertToPayment(paymentSaveRequest);
            Payment createdPayment = paymentService.createPayment(payment.getVisit().getVisitId(), payment.getSelectedCurrency(), payment.getExchangeRate());
            return "redirect:/payments/" + createdPayment.getPaymentId();

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{paymentId}/update")
    public String showUpdatePaymentPage(@PathVariable Long paymentId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            PaymentResponse paymentResponse = paymentMapper.convertToPaymentResponse(payment);
            PaymentSaveRequest paymentSaveRequest = new PaymentSaveRequest();

            paymentSaveRequest.setSelectedCurrency(paymentResponse.getSelectedCurrency());
            paymentSaveRequest.setDateOfPayment(paymentResponse.getDateOfPayment());
            paymentSaveRequest.setPaymentStatus(paymentResponse.getPaymentStatus());
            paymentSaveRequest.setVisitId(paymentResponse.getVisit().getVisitId());
            paymentSaveRequest.setVat(paymentResponse.getVat());
            paymentSaveRequest.setDiscount(paymentResponse.getDiscount());

            model.addAttribute("paymentId", paymentId);
            model.addAttribute("paymentSaveRequest", paymentSaveRequest);
            return "PaymentUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{paymentId}/update")
    public String updatePayment(@PathVariable Long paymentId, @Valid @ModelAttribute("paymentSaveRequest") PaymentSaveRequest paymentSaveRequest, BindingResult bindingResult, Model model, HttpSession session) {
        User checkUser;
        try {
            checkUser = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "PaymentUpdateView";
        }

        try {
            Payment payment = paymentMapper.convertToPayment(paymentSaveRequest);
            Payment paymentToBeUpdated = paymentService.getPaymentById(paymentId);
            paymentToBeUpdated.setVisit(payment.getVisit());
            paymentToBeUpdated.setSelectedCurrency(payment.getSelectedCurrency());
            paymentToBeUpdated.setDateOfPayment(payment.getDateOfPayment());
            paymentToBeUpdated.setPaymentStatus(payment.getPaymentStatus());
            paymentToBeUpdated.setVisit(payment.getVisit());
            paymentToBeUpdated.setVat(payment.getVat());
            paymentToBeUpdated.setDiscount(payment.getDiscount());

            return "redirect:/payments/{paymentId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{paymentId}/book-payment")
    public String bookPayment(@PathVariable Long paymentId, Model model, HttpSession session) {
        User checkUser;
        try {
            checkUser = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            paymentService.bookPayment(paymentId);
            return "redirect:/payments/{paymentId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
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



    private static void checkAccessPermissions(User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}

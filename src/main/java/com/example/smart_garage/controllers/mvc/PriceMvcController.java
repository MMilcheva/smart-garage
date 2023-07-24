package com.example.smart_garage.controllers.mvc;

import com.example.smart_garage.dto.*;
import com.example.smart_garage.exceptions.AuthenticationFailureException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.PriceMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.CarMaintenanceService;
import com.example.smart_garage.services.contracts.PriceService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@Controller
@RequestMapping("/prices")
public class PriceMvcController {


    private final PriceService priceService;
    private final CarMaintenanceService carMaintenanceService;
    private final AuthenticationHelper authenticationHelper;

    private final PriceMapper priceMapper;

    public PriceMvcController(PriceService priceService, CarMaintenanceService carMaintenanceService,
                              AuthenticationHelper authenticationHelper, PriceMapper priceMapper) {
        this.priceService = priceService;
        this.carMaintenanceService = carMaintenanceService;
        this.authenticationHelper = authenticationHelper;
        this.priceMapper = priceMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @GetMapping
    public String filterAllPrices(@ModelAttribute("priceFilterOption") PriceFilterDto priceFilterDto,
                                  Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";

        }
        PriceFilterOptions priceFilterOptions = new PriceFilterOptions(
                priceFilterDto.getAmount(),
                priceFilterDto.getValidOn(),
                priceFilterDto.getCarMaintenanceName(),
                priceFilterDto.getCarMaintenanceId(),
                priceFilterDto.getSortBy(),
                priceFilterDto.getSortOrder());

        model.addAttribute("prices", priceService.getAllPricesFilter(priceFilterOptions));
        model.addAttribute("priceFilterOptions", priceFilterDto);

        return "PricesView";
    }

    @GetMapping("/{priceId}")
    public String showSinglePrice(@PathVariable Long priceId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Price price = priceService.getPriceById(priceId);
            model.addAttribute("price", price);
            return "PriceView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/new")
    public String showCreatePricePage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        Price price = new Price();
        model.addAttribute("price", price);
        model.addAttribute("carMaintenanceOptions", carMaintenanceService.getAllCarMaintenanceOptions());
        model.addAttribute("priceSaveRequest", new PriceSaveRequest());
        return "PriceCreateView";
    }

    @PostMapping("/new")
    public String createPrice(@Valid @ModelAttribute("priceSaveRequest") PriceSaveRequest priceSaveRequest,
                              BindingResult bindingResult,
                              Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "PriceCreateView";
        }

        try {
            Price price = priceMapper.convertToPrice(priceSaveRequest);
            Price createdPrice = priceService.createPrice(price, user);
            return "redirect:/prices/" + createdPrice.getPriceId();

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";

        }
    }


    @GetMapping("/{priceId}/update")
    public String showUpdatePricePage(@PathVariable Long priceId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            Price price = priceService.getPriceById(priceId);

            PriceResponse priceResponse = priceMapper.convertToPriceResponse(price);
            PriceSaveRequest priceSaveRequest = new PriceSaveRequest();
            priceSaveRequest.setCarMaintenanceId(price.getCarMaintenance().getCarMaintenanceId());
            priceSaveRequest.setAmount(priceResponse.getAmount());
            priceSaveRequest.setValidFrom(priceResponse.getValidFrom());
            priceSaveRequest.setValidTo(priceResponse.getValidTo());
            model.addAttribute("carMaintenanceOptions", carMaintenanceService.getAllCarMaintenanceOptions());
            model.addAttribute("priceId", priceId);
            model.addAttribute("priceSaveRequest", priceSaveRequest);
            return "PriceUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @PostMapping("/{priceId}/update")
    public String updateVisit(@PathVariable Long priceId,
                              @Valid @ModelAttribute("priceSaveRequest") PriceSaveRequest priceSaveRequest,
                              BindingResult bindingResult,
                              Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "PriceUpdateView";
        }
        try {
            Price price = priceMapper.convertToPrice(priceSaveRequest);
            Price priceToBeUpdated = priceService.getPriceById(priceId);
            priceToBeUpdated.setAmount(price.getAmount());
            priceToBeUpdated.setCarMaintenance(price.getCarMaintenance());
            priceToBeUpdated.setValidFrom(price.getValidFrom());
            priceToBeUpdated.setValidTo(price.getValidTo());

            return "redirect:/prices/{priceId}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e.getMessage());
            return "DuplicateEntityView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";
        }
    }

    @GetMapping("/{priceId}/delete")
    public String deletePrice(@PathVariable Long priceId, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        try {
            priceService.deletePrice(priceId, user);
            return "redirect:/prices";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
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

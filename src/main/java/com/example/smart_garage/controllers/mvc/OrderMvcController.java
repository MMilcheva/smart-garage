package com.example.smart_garage.controllers.mvc;

import com.example.smart_garage.dto.OrderFilterDto;
import com.example.smart_garage.dto.OrderSaveRequest;
import com.example.smart_garage.exceptions.AuthenticationFailureException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.OrderMapper;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.CarMaintenanceService;
import com.example.smart_garage.services.contracts.OrderService;
import com.example.smart_garage.services.contracts.VisitService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;


@Controller
@RequestMapping("/orders")
public class OrderMvcController {


    private final OrderService orderService;
    private final VisitService visitService;
    private final CarMaintenanceService carMaintenanceService;
    private final AuthenticationHelper authenticationHelper;
    private final OrderMapper orderMapper;

    public OrderMvcController(OrderService orderService, VisitService visitService,
                              CarMaintenanceService carMaintenanceService, AuthenticationHelper authenticationHelper,
                              OrderMapper orderMapper) {
        this.orderService = orderService;
        this.visitService = visitService;
        this.carMaintenanceService = carMaintenanceService;
        this.authenticationHelper = authenticationHelper;
        this.orderMapper = orderMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @GetMapping
    public String filterAllOrders(@ModelAttribute("orderFilterOptions") OrderFilterDto orderFilterDto,
                                  Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        String sortBy = orderFilterDto.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "orderName";
        }

        OrderFilterOptions orderFilterOptions = new OrderFilterOptions(
                orderFilterDto.getOrderId(),
                orderFilterDto.getUserId(),
                orderFilterDto.getOrderName(),
                orderFilterDto.getDateOfCreation(),
                orderFilterDto.getCarMaintenanceName(),
                orderFilterDto.getCurrencyCode(),
                orderFilterDto.getVisitId(),
                orderFilterDto.getPlate(),
                orderFilterDto.getPriceAmount(),
                orderFilterDto.getSortBy(),
                orderFilterDto.getSortOrder());
        model.addAttribute("canCreateOrder", true);

        model.addAttribute("orders", orderService.getAllOrdersFilter(orderFilterOptions));

        return "OrdersView";
    }

    @GetMapping("/my-orders")
    public String filterAllOrdersByUser(@ModelAttribute("orderFilterOptions") OrderFilterDto orderFilterDto,
                                        Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        String sortBy = orderFilterDto.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "orderName";
        }
        orderFilterDto.setUserId(user.getUserId());
        OrderFilterOptions orderFilterOptions = new OrderFilterOptions(
                orderFilterDto.getOrderId(),
                orderFilterDto.getUserId(),
                orderFilterDto.getOrderName(),
                orderFilterDto.getDateOfCreation(),
                orderFilterDto.getCarMaintenanceName(),
                orderFilterDto.getCurrencyCode(),
                orderFilterDto.getVisitId(),
                orderFilterDto.getPlate(),
                orderFilterDto.getPriceAmount(),
                orderFilterDto.getSortBy(),
                orderFilterDto.getSortOrder());
        model.addAttribute("canCreateOrder", false);
        model.addAttribute("orders", orderService.getAllOrdersFilter(orderFilterOptions));

        return "OrdersView";
    }

    @GetMapping("/new")
    public String showCreateOrderPage(@ModelAttribute("orderSaveRequest") OrderSaveRequest orderSaveRequest, Model model, HttpSession session, @RequestParam(required = false) Long visitId) {
        try {
            User user = authenticationHelper.tryGetUserWithSession(session);
            checkAccessPermissions(user);
            Order order = new Order();

            if (visitId != null) {
                Visit visit = visitService.getVisitById(visitId);
                order.setVisit(visit);
            }

            model.addAttribute("order", order);
            model.addAttribute("visits", visitService.getAllVisits());
            model.addAttribute("carMaintenanceOptions", carMaintenanceService.getAllCarMaintenanceOptions());
            model.addAttribute("orderSaveRequest", orderSaveRequest);
            return "OrderCreateView";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";

        }
    }

    @PostMapping("/new")
    public String createOrder(@ModelAttribute("orderSaveRequest") OrderSaveRequest orderSaveRequest, BindingResult bindingResult, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
            //  checkAccessPermissions(user);

        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";

        }

        if (bindingResult.hasErrors()) {
            return "OrderCreateView";
        }

        try {
            Order order = orderMapper.convertToOrder(orderSaveRequest);
            Order savedOrder = orderService.createOrder(order.getVisit().getVisitId(), order.getCarMaintenance().getCarMaintenanceId());
            return "redirect:/orders/" + savedOrder.getOrderId();

        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{orderId}")
    public String showSingleOrder(@PathVariable Long orderId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            Order order = orderService.getOrderById(orderId);
            model.addAttribute("order", order);
            return "OrderView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{orderId}/delete")
    public String delete(@RequestHeader HttpHeaders headers, @PathVariable Long orderId, HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUserWithSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

        try {
            checkAccessPermissions(user);
            orderService.deleteOrder(orderId, user);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";

        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "AccessDeniedView";

        }
        return "redirect:/orders";
    }

    private static void checkAccessPermissions(User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}

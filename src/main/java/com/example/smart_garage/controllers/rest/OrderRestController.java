package com.example.smart_garage.controllers.rest;

import com.example.smart_garage.dto.OrderResponse;
import com.example.smart_garage.dto.OrderSaveRequest;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.helpers.AuthenticationHelper;
import com.example.smart_garage.helpers.OrderMapper;
import com.example.smart_garage.models.Order;
import com.example.smart_garage.models.User;
import com.example.smart_garage.services.contracts.CarMaintenanceService;
import com.example.smart_garage.services.contracts.OrderService;
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
@RequestMapping("/api/orders")
public class OrderRestController {
    private final OrderService orderService;
    private final VisitService visitService;
    private final CarMaintenanceService carMaintenanceService;
    private final AuthenticationHelper authenticationHelper;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderRestController(OrderService orderService, VisitService visitService, CarMaintenanceService carMaintenanceService, AuthenticationHelper authenticationHelper, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.visitService = visitService;
        this.carMaintenanceService = carMaintenanceService;
        this.authenticationHelper = authenticationHelper;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public List<OrderResponse> getAllOrders(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<String> search) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<Order> orders = orderService.getAllOrders(search);
            return orderMapper.convertToOrderResponses(orders);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@RequestHeader HttpHeaders headers, @PathVariable Long orderId) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            OrderResponse orderResponse = orderMapper.convertToOrderResponse(orderService.getOrderById(orderId));
            return ResponseEntity.ok(orderResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/my-orders-by-visit")
    public List<OrderResponse> getAllOrdersByVisitId(@RequestHeader HttpHeaders headers, @RequestBody Long visitId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            List<Order> orders = orderService.getAllOrdersByVisitId(visitId);
            return orderMapper.convertToOrderResponses(orders);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/my-orders")
    public List<OrderResponse> getAllOrdersByUserId(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            List<Order> orders = orderService.getAllOrdersByUserId(user.getUserId());
            return orderMapper.convertToOrderResponses(orders);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @PostMapping("/visits/{visitId}/orders")
    public ResponseEntity<OrderResponse> createOrderToVisit(@RequestHeader HttpHeaders headers, @PathVariable Long visitId, @RequestParam Long carMaintenanceId) {
        try { //TODO user cannot create post if role is blocked
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

            Order savedOrder = orderService.createOrder(visitId, carMaintenanceId);

            OrderResponse orderResponse = orderMapper.convertToOrderResponse(savedOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("orders/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@RequestHeader HttpHeaders headers,
                                                     @PathVariable Long orderId,
                                                     @Valid @RequestBody OrderSaveRequest orderSaveRequest) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);

//            Order order = orderMapper.convertToOrderToBeUpdated(orderId, orderSaveRequest);
            Order order = null;
            Order savedOrder = orderService.updateOrder(order);
//
            OrderResponse orderResponse = orderMapper.convertToOrderResponse(savedOrder);
            return ResponseEntity.ok(orderResponse);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("orders/{orderId}")
    public void delete(@RequestHeader HttpHeaders headers,  @PathVariable Long orderId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(user);
            orderService.deleteOrder(orderId, user);
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

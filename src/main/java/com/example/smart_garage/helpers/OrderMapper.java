package com.example.smart_garage.helpers;

import com.example.smart_garage.dto.BrandResponse;
import com.example.smart_garage.dto.OrderResponse;
import com.example.smart_garage.dto.OrderSaveRequest;
import com.example.smart_garage.models.Brand;
import com.example.smart_garage.models.CarMaintenance;
import com.example.smart_garage.models.Order;
import com.example.smart_garage.models.Visit;
import com.example.smart_garage.services.contracts.CarMaintenanceService;
import com.example.smart_garage.services.contracts.OrderService;
import com.example.smart_garage.services.contracts.VisitService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {
    private final OrderService orderService;
    private final CarMaintenanceService carMaintenanceService;
    private final VisitService visitService;

    public OrderMapper(OrderService orderService, CarMaintenanceService carMaintenanceService, VisitService visitService) {
        this.orderService = orderService;
        this.carMaintenanceService = carMaintenanceService;
        this.visitService = visitService;
    }

     public Order convertToOrder(OrderSaveRequest orderSaveRequest) {
        Order order = new Order();

        CarMaintenance carMaintenance = carMaintenanceService.getCarMaintenanceByName(orderSaveRequest.getCarMaintenanceName());
        Visit visit = visitService.getVisitById(orderSaveRequest.getVisitId());
        order.setCarMaintenance(carMaintenance);
        order.setVisit(visit);

        return order;
    }

    public OrderResponse convertToOrderResponse(Order order) {

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setDateOfCreation(order.getDateOfCreation());
        orderResponse.setVisit(order.getVisit());
        orderResponse.setCarMaintenance(order.getCarMaintenance());
        orderResponse.setPrice(order.getPrice());
        orderResponse.setCurrency(order.getCurrency());
        return orderResponse;
    }

    public List<OrderResponse> convertToOrderResponses(List<Order> orders) {

        List<OrderResponse> orderResponses = new ArrayList<>();

        orders.forEach(o -> orderResponses.add(convertToOrderResponse(o)));
        return orderResponses;
    }


}

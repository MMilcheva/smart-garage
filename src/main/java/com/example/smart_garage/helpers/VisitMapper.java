package com.example.smart_garage.helpers;

import com.example.smart_garage.dto.VisitResponse;
import com.example.smart_garage.dto.VisitSaveRequest;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.OrderService;
import com.example.smart_garage.services.contracts.PaymentService;
import com.example.smart_garage.services.contracts.UserService;
import com.example.smart_garage.services.contracts.VehicleService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VisitMapper {
    private final VehicleService vehicleService;
    private final PaymentService paymentService;
    private final OrderService orderService;

    private final UserService userService;


    public VisitMapper(VehicleService vehicleService, PaymentService paymentService, OrderService orderService, UserService userService) {
        this.vehicleService = vehicleService;
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.userService = userService;
    }

    //TODO StartDateOfVisit????
    public Visit convertToVisit(VisitSaveRequest visitSaveRequest) {
        Visit visit = new Visit();
        Vehicle vehicle = vehicleService.getVehicleByPlate(visitSaveRequest.getPlate());
        visit.setVehicle(vehicle);
        visit.setNotes(visitSaveRequest.getNotes());
        return visit;
    }
    public VisitResponse convertToVisitResponse(Visit visit) {

        VisitResponse visitResponse = new VisitResponse();
        visitResponse.setVisitId(visit.getVisitId());
        visitResponse.setStartDateOfVisit(visit.getStartDateOfVisit());
        visitResponse.setEndDateOfVisit(visit.getEndDateOfVisit());

        visitResponse.setVehicle(visit.getVehicle());
        User user = userService.getUserById(visit.getVehicle().getUser().getUserId());
        visitResponse.setUser(user);
        visitResponse.setArchived(visit.getArchived());
        visitResponse.setNotes(visit.getNotes());
        if (visit.getPayment() != null) {
            visitResponse.setPaymentId(visit.getPayment().getPaymentId());
        }
        List<Order> orders = orderService.getAllOrdersByVisitId(visit.getVisitId());
        StringBuilder sb = new StringBuilder();

        for (Order order : orders) {
            String name = order.getCarMaintenance().getCarMaintenanceName();
            sb.append(name).append(System.lineSeparator());
        }
        visitResponse.setOrders(sb.toString());
        return visitResponse;
    }

    public List<VisitResponse> convertToVisitResponses(List<Visit> visits) {

        List<VisitResponse> visitResponses = new ArrayList<>();

        visits.forEach(visit -> visitResponses.add(convertToVisitResponse(visit)));
        return visitResponses;
    }
}

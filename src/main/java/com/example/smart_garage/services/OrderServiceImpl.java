package com.example.smart_garage.services;

import com.example.smart_garage.exceptions.AuthorizationException;
import com.example.smart_garage.models.*;
import com.example.smart_garage.repositories.contracts.CarMaintenanceRepository;
import com.example.smart_garage.repositories.contracts.OrderRepository;
import com.example.smart_garage.repositories.contracts.PriceRepository;
import com.example.smart_garage.repositories.contracts.VisitRepository;
import com.example.smart_garage.services.contracts.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.MODIFY_BRAND_ERROR_MESSAGE;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PriceRepository priceRepository;
    private final CarMaintenanceRepository carMaintenanceRepository;
    private final VisitRepository visitRepository;
    public OrderServiceImpl(OrderRepository orderRepository, PriceRepository priceRepository, CarMaintenanceRepository carMaintenanceRepository, VisitRepository visitRepository) {
        this.orderRepository = orderRepository;
        this.priceRepository = priceRepository;
        this.carMaintenanceRepository = carMaintenanceRepository;
        this.visitRepository = visitRepository;
    }

    @Override
    public List<Order> getAllOrders(Optional<String> search) {
        return orderRepository.getAllOrders(search);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.getById(orderId);
    }

    @Override
    public List<Order> getAllOrdersFilter(OrderFilterOptions orderFilterOptions) {
        return orderRepository.getAllOrdersFilter(orderFilterOptions);
    }


    @Override
    public Order createOrder(Long visitId, Long carMaintenanceId) {
        Order order = new Order();
        Visit visit = visitRepository.getById(visitId);
        order.setVisit(visit);
        CarMaintenance carMaintenance = carMaintenanceRepository.getById(carMaintenanceId);
        order.setCarMaintenance(carMaintenance);
        LocalDate currentDate = LocalDate.now();

        Price price = priceRepository.getPriceValidOn(carMaintenanceId, currentDate);
        if(price==null){
            throw new UnsupportedOperationException("The Car maintenance has no valid price");
        }
        order.setPrice(price);
        orderRepository.create(order);
        return order;
    }

    @Override
    public List<Order> getAllOrdersByVisitId(Long visitId) {
        return orderRepository.getAllOrdersByVisitId(visitId);
    }

    @Override
    public Double calculateSumOfAllOrdersByVisitId(Long visitId) {
        return orderRepository.calculateSumOfAllOrdersByVisitId(visitId);
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.getAllOrdersByUserId(userId);
    }

    @Override
    public Order updateOrder(Order order) {
        orderRepository.update(order);
        return order;
    }

    @Override
    public void deleteOrder(long orderId, User user) {
        checkModifyPermissions(user);
        orderRepository.delete(orderId);
    }

    private void checkModifyPermissions(User user) {
        String str = "admin";
        //TODO to check why throw exc while equals is applied
        if (!(user.getRole().getRoleName().equals(str))) {
            throw new AuthorizationException(MODIFY_BRAND_ERROR_MESSAGE);
        }
    }

}



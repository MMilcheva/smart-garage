package com.example.smart_garage.services.contracts;

import com.example.smart_garage.models.Order;
import com.example.smart_garage.models.OrderFilterOptions;
import com.example.smart_garage.models.User;
import com.example.smart_garage.models.Visit;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders(Optional<String> search);

    Order getOrderById(@PathVariable Long orderId);

    List<Order> getAllOrdersFilter(OrderFilterOptions orderFilterOptions);

    Order createOrder(Long visitId, Long carMaintenanceId);

    List<Order> getAllOrdersByVisitId(@PathVariable Long visitId);

    Double calculateSumOfAllOrdersByVisitId(Long visitId);

    List<Order> getAllOrdersByUserId(Long userId);

    Order updateOrder(Order order);

    void deleteOrder(long orderId, User user);
}


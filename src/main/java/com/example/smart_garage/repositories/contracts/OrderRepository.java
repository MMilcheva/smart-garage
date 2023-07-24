package com.example.smart_garage.repositories.contracts;

import com.example.smart_garage.models.Order;
import com.example.smart_garage.models.OrderFilterOptions;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends BaseCRUDRepository<Order>  {

    List<Order> getAllOrders(Optional<String> search);


    Order getOrderById(Long orderId);

    List<Order> getAllOrdersByVisitId(Long visitId);

    double calculateSumOfAllOrdersByVisitId(Long visitId);

    List<Order> getAllOrdersByUserId(Long visitId);

    List<Order> getAllOrdersFilter(OrderFilterOptions orderFilterOptions);

    List<Order> filter(Optional<String> orderName,
                       Optional<LocalDate> dateOfCreation,
                       Optional<Long> userId,
                       Optional<String> carMaintenanceName,
                       Optional<String> currencyCode,
                       Optional<Long> visitId,
                       Optional<String> plate,
                       Optional<Double> priceAmount,
                       Optional<String> sortBy,
                       Optional<String> sortOrder);
}

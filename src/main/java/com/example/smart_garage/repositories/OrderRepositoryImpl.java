package com.example.smart_garage.repositories;

import com.example.smart_garage.models.Order;
import com.example.smart_garage.models.OrderFilterOptions;
import com.example.smart_garage.repositories.contracts.OrderRepository;
import com.example.smart_garage.repositories.contracts.VisitRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class OrderRepositoryImpl extends AbstractCRUDRepository<Order> implements OrderRepository {
    private final VisitRepository visitRepository;
    @Autowired
    public OrderRepositoryImpl(SessionFactory sessionFactory, VisitRepository visitRepository) {
        super(Order.class, sessionFactory);
        this.visitRepository = visitRepository;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Order> getAllOrders(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }

        try (Session session = sessionFactory.openSession()) {
            Query<Order> list = session.createQuery(" from Order where " +
                    "carMaintenance.carMaintenanceName like :carMaintenanceName ");
            list.setParameter("carMaintenanceName", "%" + search.get() + "%");

            return list.list();
        }
    }

    @Override
    public Order getOrderById(Long orderId) {
        return getOrderById(orderId);
    }

    @Override
    public List<Order> getAllOrdersByVisitId(Long visitId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Order> list = session.createQuery(" from Order where visit.visitId = :visitId ", Order.class);
            list.setParameter("visitId", visitId);
            return list.list();
        }
    }

    @Override
    public double calculateSumOfAllOrdersByVisitId(Long visitId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Double> query = session.createQuery(
                    "select coalesce(sum(o.price.amount), 0) from Order o where o.visit.visitId = :visitId",
                    Double.class
            );
            query.setParameter("visitId", visitId);
            Double result = query.uniqueResult();
            return result != null ? result.doubleValue() : 0.0;
        }
    }
    @Override
    public List<Order> getAllOrdersByUserId(Long userId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery(" select o from Order o, Visit v, Vehicle veh, User u where o.visit.visitId=v.visitId and v.vehicle.vehicleId=veh.vehicleId and veh.user.userId=:userId", Order.class);
            query.setParameter("userId", userId);
            List<Order> myList = query.list();
            return myList;
        }
    }

    @Override
    public List<Order> getAllOrdersFilter(OrderFilterOptions orderFilterOptions) {
        return filter(orderFilterOptions.getOrderName(),
                orderFilterOptions.getDateOfCreation(),
                orderFilterOptions.getUserId(),
                orderFilterOptions.getCarMaintenanceName(),
                orderFilterOptions.getCurrencyCode(),
                orderFilterOptions.getVisitId(),
                orderFilterOptions.getPlate(),
                orderFilterOptions.getPriceAmount(),
                orderFilterOptions.getSortBy(),
                orderFilterOptions.getSortOrder());
    }

    @Override
    public List<Order> filter(Optional<String> orderName,
                              Optional<LocalDate> dateOfCreation,
                              Optional<Long> userId,
                              Optional<String> carMaintenanceName,
                              Optional<String> currencyCode,
                              Optional<Long> visitId,
                              Optional<String> plate,
                              Optional<Double> priceAmount,
                              Optional<String> sortBy,
                              Optional<String> sortOrder) {

        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" Select o from Order o ");
            ArrayList<String> filter = new ArrayList<>();
            Map<String, Object> queryParams = new HashMap<>();

            orderName.ifPresent(value -> {
                filter.add(" o.orderName like :orderName ");
                queryParams.put("orderName", "%" + value + "%");
            });

            dateOfCreation.ifPresent(value -> {
                filter.add(" o.dateOfCreation like :dateOfCreation ");
                queryParams.put("dateOfCreation", value);
            });

            carMaintenanceName.ifPresent(value -> {
                filter.add(" o.carMaintenance.carMaintenanceName like :carMaintenanceName ");
                queryParams.put("carMaintenanceName", "%" + value + "%");
            });

            plate.ifPresent(value -> {
                filter.add(" o.visit.vehicle.plate like :plate ");
                queryParams.put("plate", "%" + value + "%");
            });
            visitId.ifPresent(value -> {
                filter.add(" o.visit.visitId = :visitId ");
                queryParams.put("visitId", value);
            });

            userId.ifPresent(value -> {
                filter.add(" o.visit.vehicle.user.userId = :userId ");
                queryParams.put("userId", value);
            });
            if (!filter.isEmpty()) {
                queryString.append(" where ").append(String.join(" and ", filter));
            }

            if (sortBy.isPresent() && !sortBy.get().isEmpty()) {
                String sortOrderString = sortOrder.orElse("ASC");
                queryString.append(" ORDER BY o.").append(sortBy.get()).append(" ").append(sortOrderString);
            }
            Query<Order> queryList = session.createQuery(queryString.toString(), Order.class);
            queryList.setProperties(queryParams);

            return queryList.list();
        }
    }
}

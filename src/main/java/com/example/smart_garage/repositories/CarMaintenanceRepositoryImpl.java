package com.example.smart_garage.repositories;


import com.example.smart_garage.models.CarMaintenance;
import com.example.smart_garage.models.CarMaintenanceFilterOptions;
import com.example.smart_garage.repositories.contracts.CarMaintenanceRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CarMaintenanceRepositoryImpl
        extends AbstractCRUDRepository<CarMaintenance> implements CarMaintenanceRepository {
    public CarMaintenanceRepositoryImpl(SessionFactory sessionFactory) {
        super(CarMaintenance.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<CarMaintenance> getAllCarMaintenances(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<CarMaintenance> query = session.createQuery(" from CarMaintenance where " +
                    "carMaintenanceName like :carMaintenanceName ");
            query.setParameter("carMaintenanceName", "%" + search.get() + "%");

            return query.list();
        }
    }

    @Override
    public List<CarMaintenance> getAllCarMaintenancesByUsername(String username) {

        try (Session session = sessionFactory.openSession()) {
            Query<CarMaintenance> query = session.createQuery(" select c from CarMaintenance c, Order o, Visit v, Vehicle veh, User u where c.carMaintenanceId = o.carMaintenance.carMaintenanceId and o.visit.visitId=v.visitId and v.vehicle.vehicleId=veh.vehicleId and veh.user.username=:username", CarMaintenance.class);
            query.setParameter("username", username);
            return query.list();
        }
    }
    @Override
    public List<CarMaintenance> getAllCarMaintenancesByUserId(Long userId) {

        try (Session session = sessionFactory.openSession()) {
            Query<CarMaintenance> query = session.createQuery(" select c from CarMaintenance c, Order o, Visit v, Vehicle veh, User u where c.carMaintenanceId = o.carMaintenance.carMaintenanceId and o.visit.visitId=v.visitId and v.vehicle.vehicleId=veh.vehicleId and veh.user.userId=:userId", CarMaintenance.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<CarMaintenance> getAllCarMaintenanceFilter(CarMaintenanceFilterOptions carMaintenanceFilterOptions) {
        return filter(carMaintenanceFilterOptions.getCarMaintenanceName(),
                carMaintenanceFilterOptions.getUsername(),
                carMaintenanceFilterOptions.getArchived(),
                carMaintenanceFilterOptions.getSortBy(),
                carMaintenanceFilterOptions.getSortOrder());
    }

    @Override
    public List<CarMaintenance> filter(Optional<String> carMaintenanceName,
                                       Optional<String> username,
                                       Optional<Boolean> isArchived,
                                       Optional<String> sortBy,
                                       Optional<String> sortOrder) {

        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" Select c from CarMaintenance c ");
            ArrayList<String> filter = new ArrayList<>();
            Map<String, Object> queryParams = new HashMap<>();
            carMaintenanceName.ifPresent(value -> {
                filter.add(" c.carMaintenanceName like :carMaintenanceName ");
                queryParams.put("carMaintenanceName", "%" + value + "%");
            });

            if (isArchived.isPresent()) {
                filter.add(" c.isArchived = :isArchived ");
                queryParams.put("isArchived", isArchived.get());
            }
            if (!filter.isEmpty()) {
                queryString.append(" where ").append(String.join(" and ", filter));
            }

            if (sortBy.isPresent() && !sortBy.get().isEmpty()) {
                String sortOrderString = sortOrder.orElse("ASC");
                queryString.append(" ORDER BY c.").append(sortBy.get()).append(" ").append(sortOrderString);
            }
            Query<CarMaintenance> queryList = session.createQuery(queryString.toString(), CarMaintenance.class);
            queryList.setProperties(queryParams);
            return queryList.list();
        }
    }
}



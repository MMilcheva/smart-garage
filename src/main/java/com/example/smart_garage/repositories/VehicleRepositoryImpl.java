package com.example.smart_garage.repositories;

import com.example.smart_garage.models.Vehicle;
import com.example.smart_garage.models.VehicleFilterOptions;
import com.example.smart_garage.models.Visit;
import com.example.smart_garage.repositories.contracts.VehicleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class VehicleRepositoryImpl extends AbstractCRUDRepository<Vehicle> implements VehicleRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public VehicleRepositoryImpl(SessionFactory sessionFactory) {
        super(Vehicle.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Vehicle> getAllVehiclesByUserId(Long userId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Vehicle> query = session.createQuery(" select veh from Vehicle veh, User u where  veh.user.userId=:userId", Vehicle.class);
            query.setParameter("userId", userId);

            return query.list();
        }
    }

    @Override
    public List<Vehicle> getAllVehicles(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Vehicle> query = session.createQuery(" from Vehicle where " +
                    "plate like :plate or vin like :vin or user.username like :username ");
            query.setParameter("plate", "%" + search.get() + "%");
            query.setParameter("vin", "%" + search.get() + "%");
            query.setParameter("username", "%" + search.get() + "%");
            return query.list();
        }
    }

    private Double getNumberIfPresent(Optional<String> search) {
        try {
            return Double.parseDouble(search.get());
        } catch (NumberFormatException e) {
            return -1.0;
        }

    }

    @Override
    public List<Vehicle> getAllVehiclesFilter(VehicleFilterOptions vehicleFilterOptions) {
        return filter(vehicleFilterOptions.getVehicleId(),
                vehicleFilterOptions.getUserId(),
                vehicleFilterOptions.getPlate(),
                vehicleFilterOptions.getVin(),
                vehicleFilterOptions.getYearOfCreation(),
                vehicleFilterOptions.getModelName(),
                vehicleFilterOptions.getUsername(),
                vehicleFilterOptions.getOwnerFirstName(),
                vehicleFilterOptions.getOwnerLatName(),
                vehicleFilterOptions.getIsArchived(),
                vehicleFilterOptions.getSortBy(),
                vehicleFilterOptions.getSortOrder());
    }

    @Override
    public List<Vehicle> filter(Optional<Long> vehicleId,
                                Optional<Long> userId,
                                Optional<String> plate,
                                Optional<String> vin,
                                Optional<Integer> yearOfCreation,
                                Optional<String> modelName,
                                Optional<String> username,
                                Optional<String> ownerFirstName,
                                Optional<String> ownerLastName,
                                Optional<Boolean> isArchived,
                                Optional<String> sortBy,
                                Optional<String> sortOrder) {

        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" Select v from Vehicle v ");
            ArrayList<String> filter = new ArrayList<>();
            Map<String, Object> queryParams = new HashMap<>();

            vehicleId.ifPresent(value -> {
                filter.add(" v.vehicleId = :vehicleId ");
                queryParams.put("vehicleId", value);
            });

            userId.ifPresent(value -> {
                filter.add(" v.user.userId = :userId ");
                queryParams.put("userId", value);
            });

            plate.ifPresent(value -> {
                filter.add(" v.plate like :plate ");
                queryParams.put("plate", "%" + value + "%");
            });

            vin.ifPresent(value -> {
                filter.add(" v.vin like :vin ");
                queryParams.put("vin", "%" + value + "%");
            });

            yearOfCreation.ifPresent(value -> {
                filter.add(" v.yearOfCreation = :yearOfCreation ");
                queryParams.put("yearOfCreation", value);
            });

            modelName.ifPresent(value -> {
                filter.add(" v.model.modelName like :modelName ");
                queryParams.put("modelName", "%" + value + "%");
            });
            username.ifPresent(value -> {
                filter.add(" v.user.username like :username ");
                queryParams.put("username", "%" + value + "%");
            });

            ownerFirstName.ifPresent(value -> {
                filter.add(" v.user.firstName like :firstName ");
                queryParams.put("firstName", "%" + value + "%");
            });

            ownerLastName.ifPresent(value -> {
                filter.add(" v.user.lastName like :lastName ");
                queryParams.put("lastName", "%" + value + "%");
            });

            isArchived.ifPresent(value -> {
                filter.add(" v.isArchived like :isArchived ");
                queryParams.put("isArchived", value);
            });

            if (!filter.isEmpty()) {
                queryString.append(" where ").append(String.join(" and ", filter));
            }

            if (sortBy.isPresent() && !sortBy.get().isEmpty()) {
                String sortOrderString = sortOrder.orElse("ASC");
                queryString.append(" ORDER BY v.").append(sortBy.get()).append(" ").append(sortOrderString);
            }
            Query<Vehicle> queryList = session.createQuery(queryString.toString(), Vehicle.class);
            queryList.setProperties(queryParams);

            return queryList.list();
        }
    }
}

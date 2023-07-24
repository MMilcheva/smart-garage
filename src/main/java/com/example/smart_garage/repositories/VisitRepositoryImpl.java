package com.example.smart_garage.repositories;

import com.example.smart_garage.enumeration.PaymentStatus;
import com.example.smart_garage.models.Visit;
import com.example.smart_garage.models.VisitFilterOptions;
import com.example.smart_garage.repositories.contracts.VisitRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class VisitRepositoryImpl extends AbstractCRUDRepository<Visit> implements VisitRepository {
    @Autowired
    public VisitRepositoryImpl(SessionFactory sessionFactory) {
        super(Visit.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Visit> getAllVisits(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Visit> list = session.createQuery("SELECT v FROM Visit v JOIN v.vehicle vehicle JOIN vehicle.carModel carModel WHERE " +
                    "vehicle.plate LIKE :plate OR CAST(v.vehicle.yearOfCreation AS STRING ) LIKE :yearOfCreation OR " +
                    "carModel.brand.brandName LIKE :brandName OR carModel.carModelName LIKE :carModelName OR " +
                    "CAST(v.startDateOfVisit AS string) LIKE :startDateOfVisit OR v.vehicle.user.firstName LIKE :firstName OR v.vehicle.user.lastName LIKE :lastName");
            list.setParameter("firstName", "%" + search.get() + "%");
            list.setParameter("lastName", "%" + search.get() + "%");
            list.setParameter("plate", "%" + search.get() + "%");
            list.setParameter("yearOfCreation", "%" + search.get() + "%");
            list.setParameter("carModelName", "%" + search.get() + "%");
            list.setParameter("brandName", "%" + search.get() + "%");
            list.setParameter("startDateOfVisit", "%" + search.get() + "%");
            return list.list();
        }
    }

    @Override
    public List<Visit> getAllVisitsByUserId(Long userId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Visit> query = session.createQuery(" select v from Visit v, Vehicle veh, User u where " +
                    "v.vehicle.vehicleId=veh.vehicleId and veh.user.userId=:userId", Visit.class);
            query.setParameter("userId", userId);

            return query.list();
        }
    }

    @Override
    public List<Visit> getAllVisitsFilter(VisitFilterOptions visitFilterOptions) {
        return filter(visitFilterOptions.getVisitId(),
                visitFilterOptions.getUserId(),
                visitFilterOptions.getMinDate(),
                visitFilterOptions.getMaxDate(),
                visitFilterOptions.getPlate(),
                visitFilterOptions.getPaymentStatus(),
                visitFilterOptions.getNotes(),
                visitFilterOptions.getArchived(),
                visitFilterOptions.getSortBy(),
                visitFilterOptions.getSortOrder());
    }

    @Override
    public List<Visit> filter(Optional<Long> visitId,
                              Optional<Long> userId,
                              Optional<LocalDate> minDate,
                              Optional<LocalDate> maxDate,
                              Optional<String> plate,
                              Optional<PaymentStatus> paymentStatus,
                              Optional<String> notes,
                              Optional<Boolean> isArchived,
                              Optional<String> sortBy,
                              Optional<String> sortOrder) {

        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" select v from Visit v ");
            ArrayList<String> filter = new ArrayList<>();
            Map<String, Object> queryParams = new HashMap<>();

            visitId.ifPresent(value -> {
                filter.add(" v.visitId =:visitId ");
                queryParams.put("visitId", value);
            });

            userId.ifPresent(value -> {
                filter.add(" v.vehicle.user.userId =:userId ");
                queryParams.put("userId", value);
            });

            minDate.ifPresent(value -> {
                filter.add(" v.startDateOfVisit >=:minDate ");
                queryParams.put("minDate", value);
            });
            maxDate.ifPresent(value -> {
                filter.add(" v.startDateOfVisit <= :maxDate ");
                queryParams.put("maxDate", value);
            });
            plate.ifPresent(value -> {
                filter.add(" v.vehicle.plate like :plate ");
                queryParams.put("plate", "%" + value + "%");
            });

            paymentStatus.ifPresent(value -> {
                filter.add(" v.payment.paymentStatus like :paymentStatus ");
                queryParams.put("paymentStatus", value);
            });
            notes.ifPresent(value -> {
                filter.add(" v.notes like :notes ");
                queryParams.put("notes", "%" + value + "%");
            });

            if (isArchived.isPresent()) {
                filter.add(" v.isArchived = :isArchived ");
                queryParams.put("isArchived", isArchived.get());
            }

            if (!filter.isEmpty()) {
                queryString.append(" where ").append(String.join(" and ", filter));
            }

            if (sortBy.isPresent() && !sortBy.get().isEmpty()) {
                String sortOrderString = sortOrder.orElse("ASC");
                queryString.append(" ORDER BY v.").append(sortBy.get()).append(" ").append(sortOrderString);
            }
            Query<Visit> queryList = session.createQuery(queryString.toString(), Visit.class);
            queryList.setProperties(queryParams);

            return queryList.list();
        }
    }
}

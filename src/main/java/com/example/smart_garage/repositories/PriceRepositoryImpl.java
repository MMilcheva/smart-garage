package com.example.smart_garage.repositories;

import com.example.smart_garage.models.*;
import com.example.smart_garage.repositories.contracts.PriceRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class PriceRepositoryImpl extends AbstractCRUDRepository<Price> implements PriceRepository {


    private final SessionFactory sessionFactory;

    public PriceRepositoryImpl(SessionFactory sessionFactory) {
        super(Price.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Price> getAllPrices(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Price> list = session.createQuery(" from Price where " +
                    "carMaintenance.carMaintenanceName like :carMaintenanceName or" +
                    " carMaintenance.carMaintenanceId = :carMaintenanceId");

            list.setParameter("carMaintenanceName", "%" + search.get() + "%");
            list.setParameter("carMaintenanceId", search.get());

            return list.list();
        }
    }

    @Override
    public void create(Price price) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(price);
            session.getTransaction().commit();
        }
    }

    @Override
    public Price getPriceValidOn(Long carMaintenanceId, LocalDate validOn) {

        try (Session session = sessionFactory.openSession()) {
            Query<Price> list = session.createQuery(" from Price p where p.carMaintenance.carMaintenanceId = :carMaintenanceId AND p.validFrom <=:validOn and p.validTo >=:validOn  ", Price.class);
            list.setParameter("carMaintenanceId", carMaintenanceId);
            list.setParameter("validOn", validOn);
            return list.list().get(0);
        }
    }

    @Override
    public List<Price> getPriceByCarMaintenanceId(Long carMaintenanceId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Price> list = session.createQuery(" from Price p where p.carMaintenance.carMaintenanceId = :carMaintenanceId ", Price.class);
            list.setParameter("carMaintenanceId", carMaintenanceId);
            return list.list();
        }
    }

    @Override
    public List<Price> getAllPricesFilter(PriceFilterOptions priceFilterOptions) {
        return filter(
                priceFilterOptions.getAmount(),
                priceFilterOptions.getValidOn(),
                priceFilterOptions.getCarMaintenanceName(),
                priceFilterOptions.getSortBy(),
                priceFilterOptions.getSortOrder());
    }

    @Override
    public List<Price> filter(
            Optional<Double> amount,
            Optional<LocalDate> validOn,
            Optional<String> carMaintenanceName,
            Optional<String> sortBy,
            Optional<String> sortOrder) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" Select p from Price p ");
            ArrayList<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            amount.ifPresent(value -> {
                filters.add(" p.amount = :amount ");
                params.put("amount", value);
            });

            validOn.ifPresent(value -> {
                filters.add(" p.validFrom <=:validOn and p.validTo >=:validOn ");
                params.put("validOn", value);
            });

           carMaintenanceName.ifPresent(value -> {
                filters.add("p.carMaintenance.carMaintenanceName like :carMaintenanceName");
                params.put("carMaintenanceName", String.format("%%%s%%", value));
            });
            if (!filters.isEmpty()) {
                queryString
                        .append("where ")
                        .append(String.join(" and ", filters));
            }

            if (sortBy.isPresent() && !sortBy.get().isEmpty()) {
                String sortOrderString = sortOrder.orElse("ASC");
                queryString.append(" ORDER BY p.").append(sortBy.get()).append(" ").append(sortOrderString);
            }
            Query<Price> queryList = session.createQuery(queryString.toString(), Price.class);
            queryList.setProperties(params);

            return queryList.list();
        }
    }


    private String generatePriceOrderBy(PriceFilterOptions priceFilterOptions) {
        if (priceFilterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (priceFilterOptions.getSortBy().get()) {
            case "amount":
                orderBy = "amount";
                break;
            case "minDate":
                orderBy = "minDate";
                break;
            case "maxDate":
                orderBy = "maxDate";
                break;

            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (priceFilterOptions.getSortOrder().isPresent() && priceFilterOptions.getSortOrder().get().
                equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }

}

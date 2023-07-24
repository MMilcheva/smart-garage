package com.example.smart_garage.repositories;

import com.example.smart_garage.enumeration.PaymentStatus;
import com.example.smart_garage.models.*;
import com.example.smart_garage.repositories.contracts.PaymentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PaymentRepositoryImpl extends AbstractCRUDRepository<Payment> implements PaymentRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public PaymentRepositoryImpl(SessionFactory sessionFactory) {
        super(Payment.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }
    public List<Payment> getAllPayments(Optional<String> search) {
        if (search.isEmpty()) {
            return getAllPayments();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Payment> list = session.createQuery(" from Payment where " +
                    " cast(visit.visitId as string ) like :visitId or cast (dateOfPayment as string ) like :dateOfPayment ");

            list.setParameter("visitId", "%" + search.get() + "%");
            list.setParameter("dateOfPayment", "%" + search.get() + "%");

            return list.list();
        }
    }


    private List<Payment> getAllPayments() {
        try (Session session = sessionFactory.openSession()) {

            Query<Payment> list = session.createQuery(" from Payment ", Payment.class);
            return list.list();
        }
    }

    @Override
    public List<Payment> getAllPaymentsByUserId(Long userId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Payment> query = session.createQuery(" select p from Payment p, Visit v, Vehicle veh, User u where p.visit.visitId=v.visitId and v.vehicle.vehicleId=veh.vehicleId and veh.user.userId=:userId", Payment.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<Payment> getAllDuePaymentsByUserId(Long userId) {

        try (Session session = sessionFactory.openSession()) {
                       Query<Payment> query = session.createQuery(
                    "SELECT p " +
                            "FROM Payment p " +
                            "JOIN p.visit v " +
                            "JOIN v.vehicle veh " +
                            "JOIN veh.user u " +
                            "WHERE p.paymentStatus = :status " +
                            "AND u.userId = :userId", Payment.class);
            query.setParameter("status", PaymentStatus.UNPAID);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<Payment> getAllPaidPaymentsByUserId(Long userId) {

        try (Session session = sessionFactory.openSession()) {
            Query<Payment> query = session.createQuery(
                    "SELECT p " +
                            "FROM Payment p " +
                            "JOIN p.visit v " +
                            "JOIN v.vehicle veh " +
                            "JOIN veh.user u " +
                            "WHERE p.paymentStatus = :status " +
                            "AND u.userId = :userId", Payment.class);
            query.setParameter("status", PaymentStatus.PAID);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public void updatePayment(Payment payment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(payment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updatePaymentStatus(Long paymentId) {
        Payment payment;
        payment = getById(paymentId);
        payment.setPaymentStatus(PaymentStatus.UNPAID);
    }


    @Override
    public Payment createPayment(Payment payment) {
        try (Session session = sessionFactory.openSession()) {
            session.save(payment);
        }
        return payment;
    }

    @Override
    public void deletePayment(Long paymentId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(getById(paymentId));
            session.getTransaction().commit();
        }
    }

    public List<Payment> filterAllPayments(PaymentFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getUserId().ifPresent(value -> {
                filters.add(" visit.vehicle.user.userId = :userId ");
                params.put("userId", value);
            });

            filterOptions.getListPrice().ifPresent(value -> {
                filters.add("listPrice = :listPrice");
                params.put("listPrice", value);
            });

            filterOptions.getDiscount().ifPresent(value -> {
                filters.add("discount = :discount");
                params.put("discount", value);
            });

            filterOptions.getVat().ifPresent(value -> {
                filters.add("vat = :vat");
                params.put("vat", value);
            });

            filterOptions.getTotalPriceBGN().ifPresent(value -> {
                filters.add("totalPriceBGN = :totalPriceBGN");
                params.put("totalPriceBGN", value);
            });

            filterOptions.getDateOfPayment().ifPresent(value -> {
                filters.add("dateOfPayment = :dateOfPayment");
                params.put("dateOfPayment", value);
            });

            filterOptions.getPaymentStatus().ifPresent(value -> {
                filters.add("paymentStatus = :paymentStatus");
                params.put("paymentStatus", value);
            });

            filterOptions.getSelectedCurrency().ifPresent(value -> {
                filters.add("selectedCurrency = :selectedCurrency");
                params.put("selectedCurrency", value);
            });

            filterOptions.getExchangeRate().ifPresent(value -> {
                filters.add("exchangeRate = :exchangeRate");
                params.put("exchangeRate", value);
            });

            filterOptions.getIsArchived().ifPresent(value -> {
                filters.add("isArchived = :isArchived");
                params.put("isArchived", value);
            });

            filterOptions.getVisitId().ifPresent(value -> {
                filters.add("visitId = :visitId");
                params.put("visitId", value);
            });

            filterOptions.getPaymentStatus().ifPresent(value -> {
                filters.add("paymentStatus = :paymentStatus");
                params.put("paymentStatus", value);
            });

            filterOptions.getListPrice().ifPresent(value -> {
                filters.add("listPrice = :listPrice");
                params.put("listPrice", value);
            });

            filterOptions.getDiscount().ifPresent(value -> {
                filters.add("discount = :discount");
                params.put("discount", value);
            });

            filterOptions.getVat().ifPresent(value -> {
                filters.add("vat = :vat");
                params.put("vat", value);
            });

            filterOptions.getTotalPriceBGN().ifPresent(value -> {
                filters.add("totalPriceBGN = :totalPriceBGN");
                params.put("totalPriceBGN", value);
            });

            StringBuilder queryString = new StringBuilder("from Payment");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generatePaymentOrderBy(filterOptions));

            Query<Payment> query = session.createQuery(queryString.toString(), Payment.class);
            query.setProperties(params);
            return query.list();
        }
    }

    private String generatePaymentOrderBy(PaymentFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }
        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {

            case "date_of_payment":
                orderBy = "date_of_payment";
                break;
            case "payment_status":
                orderBy = "payment_status";
                break;
            case "list_price":
                orderBy = "list_price";
                break;
            case "discount":
                orderBy = "discount";
                break;
            case "vat":
                orderBy = "vat";
                break;
            case "total_price_bgn":
                orderBy = "total_price_bgn";
                break;

            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().
                equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }
        return orderBy;
    }

}

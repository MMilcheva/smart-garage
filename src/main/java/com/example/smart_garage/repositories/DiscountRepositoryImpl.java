package com.example.smart_garage.repositories;

import com.example.smart_garage.models.Discount;
import com.example.smart_garage.models.DiscountFilterOptions;
import com.example.smart_garage.repositories.contracts.DiscountRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class DiscountRepositoryImpl extends AbstractCRUDRepository<Discount> implements DiscountRepository {

    @Autowired
    public DiscountRepositoryImpl(SessionFactory sessionFactory) {
        super(Discount.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Discount> getAllDiscountsFilter(DiscountFilterOptions discountFilterOptions) {
        return filter(discountFilterOptions.getDiscountName(), discountFilterOptions.getUsername(), discountFilterOptions.getIsArchived(), discountFilterOptions.getSortBy(), discountFilterOptions.getSortOrder());
    }

    @Override
    public List<Discount> filter(Optional<String> discountName,
                                 Optional<String> username,
                                 Optional<Boolean> isArchived,
                                 Optional<String> sortBy,
                                 Optional<String> sortOrder) {

        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" SELECT d FROM Discount d ");
            ArrayList<String> filter = new ArrayList<>();
            Map<String, Object> queryParams = new HashMap<>();

            if (discountName.isPresent()) {
                filter.add(" d.discountName LIKE :discountName ");
                queryParams.put("discountName", "%" + discountName.get() + "%");
            }
            if (username.isPresent()) {
                filter.add(" d.user.username like :username ");
                queryParams.put("username", "%" + username.get() + "%");
            }
            if (isArchived.isPresent()) {
                filter.add(" d.isArchived = :isArchived ");
                queryParams.put("isArchived", isArchived.get());
            }

            if (!filter.isEmpty()) {
                queryString.append(" WHERE ").append(String.join(" AND ", filter));
            }

            if (sortBy.isPresent() && !sortBy.get().isEmpty()) {
                String sortOrderString = sortOrder.orElse("ASC");
                queryString.append(" ORDER BY d.").append(sortBy.get()).append(" ").append(sortOrderString);
            }

            Query<Discount> queryList = session.createQuery(queryString.toString(), Discount.class);
            queryList.setProperties(queryParams);

            return queryList.list();
        }
    }

    @Override
    public List<Discount> getAllDiscounts(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Discount> list = session.createQuery(" from Discount where " +

                    " cast (discountAmount as string ) like :discountAmount or " +
                    " user.username like :username ");
            list.setParameter("discountAmount", "%" + search.get() + "%");
            list.setParameter("username", "%" + search.get() + "%");
            return list.list();
        }
    }

    @Override
    public Discount getDiscountByUserValidOn(Long userId, LocalDate validOn) {
        try (Session session = sessionFactory.openSession()) {
            Query<Discount> list = session.createQuery(
                    "FROM Discount d WHERE d.user.userId = :userId AND d.validFrom <= :validOn AND d.validTo >= :validOn",
                    Discount.class
            );
            list.setParameter("userId", userId);
            list.setParameter("validOn", validOn);
            List<Discount> discounts = list.list();
            if (!discounts.isEmpty()) {
                return discounts.get(0);
            } else {
                Discount defaultDiscount = new Discount();
                defaultDiscount.setDiscountName("Default Discount");
                defaultDiscount.setDiscountAmount(0.0);
                return defaultDiscount;
            }
        }
    }

    @Override
    public List<Discount> getAllDiscountsByUserId(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Discount> query = session.createQuery
                    (" from Discount d where d.user.userId=:userId ", Discount.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }
}
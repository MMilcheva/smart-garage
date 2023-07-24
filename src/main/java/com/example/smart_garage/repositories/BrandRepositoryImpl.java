package com.example.smart_garage.repositories;

import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.models.BrandFilterOptions;
import com.example.smart_garage.models.Brand;
import com.example.smart_garage.models.User;
import com.example.smart_garage.repositories.contracts.BrandRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BrandRepositoryImpl extends AbstractCRUDRepository<Brand> implements BrandRepository {
    private final SessionFactory sessionFactory;

    public BrandRepositoryImpl(SessionFactory sessionFactory) {
        super(Brand.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Brand> getAllBrands(Optional<String> search) {
        try (Session session = sessionFactory.openSession()) {
            Query<Brand> query = session.createQuery("FROM Brand b WHERE b.isArchived = false AND b.brandName LIKE :brandName", Brand.class);
            query.setParameter("brandName", "%" + search.orElse("") + "%");
            return query.list();
        }
    }
    @Override
    public List<Brand> getAllBrandsFilter(BrandFilterOptions brandFilterOptions) {
        return filter(brandFilterOptions.getBrandName(), brandFilterOptions.getArchived(), brandFilterOptions.getSortBy(), brandFilterOptions.getSortOrder());
    }

    @Override
    public List<Brand> filter(Optional<String> brandName,
                              Optional<Boolean> isArchived,
                              Optional<String> sortBy,
                              Optional<String> sortOrder) {

        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder("SELECT b FROM Brand b");
            ArrayList<String> filter = new ArrayList<>();
            Map<String, Object> queryParams = new HashMap<>();

            if (brandName.isPresent()) {
                filter.add("b.brandName LIKE :brandName");
                queryParams.put("brandName", "%" + brandName.get() + "%");
            }

            if (isArchived.isPresent()) {
                filter.add("b.isArchived = :isArchived");
                queryParams.put("isArchived", isArchived.get());
            }

            if (!filter.isEmpty()) {
                queryString.append(" WHERE ").append(String.join(" AND ", filter));
            }


            if (sortBy.isPresent() && !sortBy.get().isEmpty()) {
                String sortOrderString = sortOrder.orElse("ASC");
                queryString.append(" ORDER BY b.").append(sortBy.get()).append(" ").append(sortOrderString);
            }

            Query<Brand> queryList = session.createQuery(queryString.toString(), Brand.class);
            queryList.setProperties(queryParams);

            return queryList.list();
        }
    }

    @Override
    public Brand findBrandByName(String brandName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Brand> query = session.createQuery("from Brand where brandName = :brandName", Brand.class);
            query.setParameter("brandName", brandName);

            List<Brand> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Brand", "brandName", brandName);
            }

            return result.get(0);
        }
    }

    @Override
    public List<Brand> findByBrandName(String brandName) {
        Session session = sessionFactory.getCurrentSession();
        Query<Brand> query = session.createQuery("FROM Brand WHERE brandName = :brandName", Brand.class);
        query.setParameter("brandName", brandName);
        return query.getResultList();
    }
}





package com.example.smart_garage.repositories;

import com.example.smart_garage.models.CarModel;
import com.example.smart_garage.models.CarModelFilterOptions;
import com.example.smart_garage.repositories.contracts.CarModelRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CarModelRepositoryImpl extends AbstractCRUDRepository<CarModel> implements CarModelRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public CarModelRepositoryImpl(SessionFactory sessionFactory) {
        super(CarModel.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<CarModel> getAllCarModels(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<CarModel> list = session.createQuery(" from CarModel where " +
                    "carModelName like :CarModelName ");
            list.setParameter("carModelName", "%" + search.get() + "%");
//TODO maybe something should be fixed additionally
            return list.list();
        }
    }
 @Override
    public List<CarModel> getAllCarModelsByBrandId(Long brandId) {

        try (Session session = sessionFactory.openSession()) {
            Query<CarModel> list = session.createQuery(" from CarModel where " +
                    "brand.brandId=:brandId ");
            list.setParameter("brandId", brandId);

            return list.list();
        }
    }

    public List<CarModel> getAllCarModelsFilter(CarModelFilterOptions carModelFilterOptions) {
        return filter(carModelFilterOptions.getCarModelId(),
                carModelFilterOptions.getCarModelName(),
                carModelFilterOptions.getBrandName(),
                carModelFilterOptions.getIsArchived(),
                carModelFilterOptions.getSortBy(),
                carModelFilterOptions.getSortOrder());
    }

    @Override
    public List<CarModel> filter(Optional<Long> modelId,
                                 Optional<String> carModelName,
                                 Optional<String> brandName,
                                 Optional<Boolean> isArchived,
                                 Optional<String> sortBy,
                                 Optional<String> sortOrder) {

        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" Select m from CarModel m ");
            ArrayList<String> filter = new ArrayList<>();
            Map<String, Object> queryParams = new HashMap<>();

            carModelName.ifPresent(value -> {
                filter.add(" m.carModelName like :carModelName ");
                queryParams.put("carModelName", "%" + value + "%");
            });

            brandName.ifPresent(value -> {
                filter.add(" m.brand.brandName like :brandName ");
                queryParams.put("brandName", "%" + value + "%");
            });

            if (isArchived.isPresent()) {
                filter.add(" m.isArchived = :isArchived ");
                queryParams.put("isArchived", isArchived.get());
            }
            if (!filter.isEmpty()) {
                queryString.append(" where ").append(String.join(" and ", filter));
            }

            if (sortBy.isPresent() && !sortBy.get().isEmpty()) {
                String sortOrderString = sortOrder.orElse("ASC");
                queryString.append(" ORDER BY m.").append(sortBy.get()).append(" ").append(sortOrderString);
            }
            Query<CarModel> queryList = session.createQuery(queryString.toString(), CarModel.class);
            queryList.setProperties(queryParams);

            return queryList.list();
        }
    }

    private String generateStringFromSort(String value) {
        StringBuilder queryString = new StringBuilder(" order by ");
        String[] params = value.split("_");

        if (value.isEmpty()) {
            return "";
        }

        switch (params[0]) {
            case "title":
                queryString.append(" p.title ");
                break;
            case "content":
                queryString.append(" p.content ");
                break;
            case "username":
                queryString.append(" u.userName ");
                break;
            case "startDate":
            case "endDate":
                queryString.append(" p.creationDate");
                break;

            default:
                throw new UnsupportedOperationException(
                        "Sort should have max two params divided by _ symbol!");
        }

        if (params.length > 2) {
            throw new UnsupportedOperationException(
                    "Sort should have max two params divided by _ symbol!");
        }

        if (params.length == 2 && params[1].equalsIgnoreCase("desc")) {
            queryString.append(" desc ");
        }
        return queryString.toString();
    }

    @Override
    public void block(Long carModelId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarModel carModel = session.load(CarModel.class, carModelId);
            carModel.setArchived(true);
            session.update(carModel);
            session.getTransaction().commit();
        }
    }
}

package com.example.smart_garage.repositories.contracts;

import com.example.smart_garage.models.CarModel;
import com.example.smart_garage.models.CarModelFilterOptions;

import java.util.List;
import java.util.Optional;

public interface CarModelRepository extends BaseCRUDRepository<CarModel> {
    List<CarModel> getAllCarModels(Optional<String> search);

    List<CarModel> filter(Optional<Long> carModelId, Optional<String> carModelName,
                          Optional<String> brandName,Optional<Boolean> isArchived,
                          Optional<String> sortBy, Optional<String> sortOrder);

    List<CarModel> getAllCarModelsByBrandId(Long brandId);

    List<CarModel> getAllCarModelsFilter(CarModelFilterOptions carModelFilterOptions);

    void block( Long carModelId);
}

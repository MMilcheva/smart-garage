package com.example.smart_garage.services.contracts;

import com.example.smart_garage.models.CarModel;
import com.example.smart_garage.models.CarModelFilterOptions;
import com.example.smart_garage.models.User;

import java.util.List;
import java.util.Optional;

public interface CarModelService {
    CarModel getCarModelById(Long modelToBeUpdatedId);

    CarModel getCarModelByName(String name);

    List<CarModel> getAllModels(Optional<String> search);

    List<CarModel> getAllCarModelsFilter(CarModelFilterOptions carModelFilterOptions);

    List<CarModel> getAllCarModelsByBrandId(Long brandId);

    void deleteCarModel(long modelId);


    void block(User user, Long carModelId);

    CarModel createCarModel(CarModel model);

    CarModel updateCarModel(CarModel model);
}

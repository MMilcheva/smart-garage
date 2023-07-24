package com.example.smart_garage.services;

import com.example.smart_garage.exceptions.AuthorizationException;
import com.example.smart_garage.models.*;
import com.example.smart_garage.repositories.contracts.BrandRepository;
import com.example.smart_garage.repositories.contracts.CarModelRepository;
import com.example.smart_garage.services.contracts.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.MODIFY_MODEL_ERROR_MESSAGE;

@Service
public class CarModelServiceImpl implements CarModelService {

    private final CarModelRepository carModelRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public CarModelServiceImpl(CarModelRepository carModelRepository, BrandRepository brandRepository) {
        this.carModelRepository = carModelRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public CarModel getCarModelById(Long modelToBeUpdatedId) {

        return carModelRepository.getById(modelToBeUpdatedId);
    }
    @Override
    public CarModel getCarModelByName(String name) {
        return carModelRepository.getByName(name);
    }

    @Override
    public List<CarModel> getAllModels(Optional<String> search) {

        return carModelRepository.getAllCarModels(search);
    }

    @Override
    public List<CarModel> getAllCarModelsFilter(CarModelFilterOptions carModelFilterOptions) {
        return carModelRepository.getAllCarModelsFilter(carModelFilterOptions);
    }
    @Override
    public List<CarModel> getAllCarModelsByBrandId(Long brandId) {
        return carModelRepository.getAllCarModelsByBrandId(brandId);
    }


    @Override
    public void deleteCarModel(long modelId) {
        carModelRepository.delete(modelId);
    }

    @Override
    public CarModel createCarModel(CarModel carModel) {
        carModelRepository.create(carModel);
        return carModel;
    }

    @Override
    public CarModel updateCarModel(CarModel carModel) {

        carModelRepository.update(carModel);
        return carModel;
    }

    @Override
    public void block(User user, Long carModelId){
        CarModel carModel = carModelRepository.getById(carModelId);
        carModel.setArchived(true);
        carModelRepository.block(carModel.getCarModelId());
    }

//    getAllModels

    private void checkModifyPermissions(User user) {
        String str = "admin";
        //TODO to check why throw exc while equals is applied
        if (!(user.getRole().getRoleName().equals(str))) {
            throw new AuthorizationException(MODIFY_MODEL_ERROR_MESSAGE);
        }
    }


}

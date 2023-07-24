package com.example.smart_garage.helpers;


import com.example.smart_garage.dto.CarModelResponse;
import com.example.smart_garage.dto.CarModelSaveRequest;
import com.example.smart_garage.models.Brand;
import com.example.smart_garage.models.CarModel;
import com.example.smart_garage.services.contracts.CarModelService;
import com.example.smart_garage.services.contracts.BrandService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarModelMapper {

    private final CarModelService carModelService;
    private final BrandService brandService;

    public CarModelMapper(CarModelService carModelService, BrandService brandService) {
        this.carModelService = carModelService;
        this.brandService = brandService;
    }


    public CarModel convertToCarModel(CarModelSaveRequest carModelSaveRequest) {
        CarModel carModel = new CarModel();
        carModel.setCarModelName(carModelSaveRequest.getCarModelName());

        Brand brand = brandService.getBrandById(carModelSaveRequest.getBrandId());
        carModel.setBrand(brand);
//        carModel.setArchived(false);
        return carModel;
    }

    public CarModelResponse convertToCarModelResponse(CarModel carModel) {

        CarModelResponse carModelResponse = new CarModelResponse();
        carModelResponse.setCarModelName(carModel.getCarModelName());
        carModelResponse.setBrandName(carModel.getBrand().getBrandName());
        return carModelResponse;
    }
    public List<CarModelResponse> convertToCarModelResponses(List<CarModel> carModels) {

        List<CarModelResponse> carModelResponses = new ArrayList<>();

        carModels.forEach(carModel -> carModelResponses.add(convertToCarModelResponse(carModel)));
        return carModelResponses;
    }

    public CarModelSaveRequest convertToCarModelSaveRequest(CarModel carModel) {
        CarModelSaveRequest carModelSaveRequest = new CarModelSaveRequest();
        carModelSaveRequest.setCarModelName(carModel.getCarModelName());
        carModelSaveRequest.setBrand(carModel.getBrand());
        return carModelSaveRequest;
    }

}

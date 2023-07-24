package com.example.smart_garage.helpers;

import com.example.smart_garage.dto.*;
import com.example.smart_garage.models.Brand;
import com.example.smart_garage.models.CarModel;
import com.example.smart_garage.models.User;
import com.example.smart_garage.models.Vehicle;
import com.example.smart_garage.services.contracts.BrandService;
import com.example.smart_garage.services.contracts.CarModelService;
import com.example.smart_garage.services.contracts.UserService;
import com.example.smart_garage.services.contracts.VehicleService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VehicleMapper {

    private final UserService userService;
    private final CarModelService carModelService;
    private final VehicleService vehicleService;

    private final BrandService brandService;

    public VehicleMapper(UserService userService, CarModelService carModelService, VehicleService vehicleService, BrandService brandService) {
        this.userService = userService;
        this.carModelService = carModelService;
        this.vehicleService = vehicleService;
        this.brandService = brandService;
    }


    public Vehicle convertToVehicle(VehicleSaveRequest vehicleSaveRequest) {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(vehicleSaveRequest.getPlate());
        vehicle.setVin(vehicleSaveRequest.getVin());
        vehicle.setYearOfCreation(vehicleSaveRequest.getYearOfCreation());
        CarModel carModel = carModelService.getCarModelById(2L);
        vehicle.setModel(carModel);
        User user = userService.getUserByEmail(vehicleSaveRequest.getEmail());
        vehicle.setUser(user);

        return vehicle;
    }

    public Vehicle convertToVisitVehicle(Long userId, VehicleSaveRequest vehicleSaveRequest) {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(vehicleSaveRequest.getPlate());
        vehicle.setVin(vehicleSaveRequest.getVin());
        vehicle.setYearOfCreation(vehicleSaveRequest.getYearOfCreation());
        return vehicle;
    }


    public VehicleResponse convertToVehicleResponse(Vehicle vehicle) {

        VehicleResponse vehicleResponse = new VehicleResponse();
        vehicleResponse.setPlate(vehicle.getPlate());
        vehicleResponse.setVin(vehicle.getVin());
        vehicleResponse.setYearOfCreation(vehicle.getYearOfCreation());
        CarModel carModel = carModelService.getCarModelById(vehicle.getModel().getCarModelId());
        vehicleResponse.setModelName(carModel.getCarModelName());
        vehicleResponse.setUsername(vehicle.getUser().getUsername());
        Brand brand = vehicle.getModel().getBrand();
        vehicleResponse.setBrandName(brand.getBrandName());
        return vehicleResponse;
    }

    public Vehicle convertToVehicleToBeUpdated(Long vehicleToBeUpdatedId, VehicleSaveRequest vehicleSaveRequest) {
        Vehicle vehicleToBeUpdated = vehicleService.getVehicleById(vehicleToBeUpdatedId);

        vehicleToBeUpdated.setPlate(vehicleSaveRequest.getPlate());
        vehicleToBeUpdated.setVin(vehicleSaveRequest.getVin());
        vehicleToBeUpdated.setYearOfCreation(vehicleSaveRequest.getYearOfCreation());
        CarModel carModel = carModelService.getCarModelById(vehicleToBeUpdated.getModel().getCarModelId());
        vehicleToBeUpdated.setModel(carModel);
        vehicleToBeUpdated.setArchived(vehicleSaveRequest.getArchived());

        return vehicleToBeUpdated;
    }

    public List<VehicleResponse> convertToVehicleResponses(List<Vehicle> vehicles) {

        List<VehicleResponse> vehicleResponses = new ArrayList<>();

        vehicles.forEach(vehicle -> vehicleResponses.add(convertToVehicleResponse(vehicle)));
        return vehicleResponses;
    }
}

package com.example.smart_garage.helpers;

import com.example.smart_garage.dto.CarMaintenanceResponse;
import com.example.smart_garage.dto.CarMaintenanceSaveRequest;
import com.example.smart_garage.models.CarMaintenance;
import com.example.smart_garage.services.contracts.CarMaintenanceService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarMaintenanceMapper {

    private final CarMaintenanceService carMaintenanceService;

    public CarMaintenanceMapper(CarMaintenanceService carMaintenanceService) {
        this.carMaintenanceService = carMaintenanceService;
    }


    public  CarMaintenance convertToCarMaintenanceToBeUpdated(Long carMaintenanceToBeUpdatedId, CarMaintenanceSaveRequest carMaintenanceSaveRequest) {
       CarMaintenance carMaintenanceToBeUpdated = carMaintenanceService.getCarMaintenanceById(carMaintenanceToBeUpdatedId);
        carMaintenanceToBeUpdated.setCarMaintenanceName(carMaintenanceSaveRequest.getCarMaintenanceName());
        carMaintenanceToBeUpdated.setArchived(carMaintenanceSaveRequest.getArchived());

        return carMaintenanceToBeUpdated;
    }

    public CarMaintenance convertToCarMaintenance(CarMaintenanceSaveRequest carMaintenanceSaveRequest) {
        CarMaintenance carMaintenance = new CarMaintenance();

        carMaintenance.setCarMaintenanceName(carMaintenanceSaveRequest.getCarMaintenanceName());
        return carMaintenance;
    }

    public CarMaintenanceResponse convertToCarMaintenanceResponse(CarMaintenance carMaintenance) {

        CarMaintenanceResponse carMaintenanceResponse = new CarMaintenanceResponse();

        carMaintenanceResponse.setCarMaintenanceName(carMaintenance.getCarMaintenanceName());
        carMaintenanceResponse.setArchived(carMaintenance.getArchived());

        return carMaintenanceResponse;
    }
    public List<CarMaintenanceResponse> convertToCarMaintenanceResponses(List<com.example.smart_garage.models.CarMaintenance> carMaintenances) {

        List<CarMaintenanceResponse> carMaintenanceResponses = new ArrayList<>();

        carMaintenances.forEach(carMaintenance-> carMaintenanceResponses.add(convertToCarMaintenanceResponse(carMaintenance)));
        return carMaintenanceResponses;
    }
}

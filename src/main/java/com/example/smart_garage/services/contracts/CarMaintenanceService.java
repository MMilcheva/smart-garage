package com.example.smart_garage.services.contracts;

import com.example.smart_garage.models.CarMaintenance;
import com.example.smart_garage.models.CarMaintenanceFilterOptions;
import com.example.smart_garage.models.User;

import java.util.List;
import java.util.Optional;

public interface CarMaintenanceService {

    CarMaintenance getCarMaintenanceById(Long carMaintenanceId);

    List<CarMaintenance> getAllCarMaintenanceOptions();

    CarMaintenance getCarMaintenanceByName(String carMaintenanceName);

    List<CarMaintenance> getAllCarMaintenances(Optional<String> search);

    List<CarMaintenance> getAllCarMaintenanceFilter(CarMaintenanceFilterOptions carMaintenanceFilterOptions);

    void deleteCarMaintenance(long carMaintenanceId, User user);

    CarMaintenance createCarMaintenance(CarMaintenance carMaintenanceService);

    CarMaintenance updateCarMaintenance(CarMaintenance carMaintenanceService);

    List<CarMaintenance> getAllCarMaintenancesByUsername(String username);

    List<CarMaintenance> getAllCarMaintenancesByUserId(Long userId);
}

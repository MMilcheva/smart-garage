package com.example.smart_garage.repositories.contracts;

import com.example.smart_garage.models.CarMaintenance;
import com.example.smart_garage.models.CarMaintenanceFilterOptions;

import java.util.List;
import java.util.Optional;

public interface CarMaintenanceRepository extends BaseCRUDRepository<CarMaintenance>{
    List<CarMaintenance> getAllCarMaintenances(Optional<String> search);

   List<CarMaintenance> getAllCarMaintenancesByUsername(String username);

    List<CarMaintenance> getAllCarMaintenancesByUserId(Long userId);

    List<CarMaintenance> getAllCarMaintenanceFilter(CarMaintenanceFilterOptions carMaintenanceFilterOptions);

    List<CarMaintenance> filter(Optional<String> carMaintenanceName,
                                Optional<String> username,
                                Optional<Boolean> isArchived,
                                Optional<String> sortBy,
                                Optional<String> sortOrder);
}

package com.example.smart_garage.repositories.contracts;

import com.example.smart_garage.models.Vehicle;
import com.example.smart_garage.models.VehicleFilterOptions;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends BaseCRUDRepository<Vehicle> {
    List<Vehicle> getAllVehiclesByUserId(Long userId);

    List<Vehicle> getAllVehicles(Optional<String> search);

    List<Vehicle> getAllVehiclesFilter(VehicleFilterOptions vehicleFilterOptions);


    List<Vehicle> filter(Optional<Long> vehicleId,
                         Optional<Long> userId,
                         Optional<String> plate,
                         Optional<String> vin,
                         Optional<Integer> yearOfCreation,
                         Optional<String> modelName,
                         Optional<String> username,
                         Optional<String> ownerFirstName,
                         Optional<String> ownerLastName,
                         Optional<Boolean> isArchived,
                         Optional<String> sortBy,
                         Optional<String> sortOrder);
}

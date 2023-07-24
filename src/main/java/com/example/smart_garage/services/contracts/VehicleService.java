package com.example.smart_garage.services.contracts;

import com.example.smart_garage.models.User;
import com.example.smart_garage.models.Vehicle;
import com.example.smart_garage.models.VehicleFilterOptions;

import java.util.List;
import java.util.Optional;

public interface VehicleService {


    Vehicle getVehicleById(Long vehicleId);

    List<Vehicle> getAllVehicles(Optional<String> search);

    List<Vehicle> getAllVehiclesFilter(VehicleFilterOptions vehicleFilterOptions);

    void deleteVehicle(long vehicleId, User user);

    Vehicle createVehicle(Vehicle vehicle);

    Vehicle updateVehicle(Vehicle vehicle);

    List<Vehicle> getAllVehiclesByUserId(Long userId);

    Vehicle getVehicleByPlate(String plate);
}

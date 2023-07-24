package com.example.smart_garage.services;

import com.example.smart_garage.exceptions.AuthorizationException;
import com.example.smart_garage.models.*;
import com.example.smart_garage.repositories.contracts.VehicleRepository;
import com.example.smart_garage.services.contracts.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.MODIFY_MODEL_ERROR_MESSAGE;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }
    @Override
    public Vehicle getVehicleById(Long vehicleId) {
        return vehicleRepository.getById(vehicleId);
    }
    @Override
    public List<Vehicle> getAllVehicles(Optional<String> search) {
        return vehicleRepository.getAllVehicles(search);
    }
    @Override
    public List<Vehicle> getAllVehiclesFilter(VehicleFilterOptions vehicleFilterOptions) {
        return vehicleRepository.getAllVehiclesFilter(vehicleFilterOptions);
    }
    @Override
    public void deleteVehicle(long vehicleId, User user) {
        checkModifyPermissions(user);
        vehicleRepository.delete(vehicleId);
    }
    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        vehicleRepository.create(vehicle);
        return vehicle;
    }
    @Override
    public Vehicle updateVehicle(Vehicle vehicle) {
        vehicleRepository.update(vehicle);
        return vehicle;
    }
    @Override
    public List<Vehicle> getAllVehiclesByUserId(Long userId) {
        return vehicleRepository.getAllVehiclesByUserId(userId);
    }

    @Override
    public Vehicle getVehicleByPlate(String plate) {
        Vehicle vehicle = vehicleRepository.getByField("plate", plate);
        return vehicle;
    }

    private void checkModifyPermissions(User user) {
        String str = "admin";
        if (!(user.getRole().getRoleName().equals(str))) {
            throw new AuthorizationException(MODIFY_MODEL_ERROR_MESSAGE);
        }
    }

}

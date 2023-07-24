package com.example.smart_garage.services;

import com.example.smart_garage.exceptions.AuthorizationException;
import com.example.smart_garage.models.CarMaintenance;
import com.example.smart_garage.models.CarMaintenanceFilterOptions;
import com.example.smart_garage.models.User;
import com.example.smart_garage.repositories.contracts.CarMaintenanceRepository;
import com.example.smart_garage.services.contracts.CarMaintenanceService;
import com.example.smart_garage.services.contracts.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.MODIFY_BRAND_ERROR_MESSAGE;

@Service
public class CarMaintenanceServiceImpl implements CarMaintenanceService {
    private final CarMaintenanceRepository carMaintenanceRepository;
private final PriceService priceService;
    @Autowired
    public CarMaintenanceServiceImpl(CarMaintenanceRepository carMaintenanceRepository, PriceService priceService) {

        this.carMaintenanceRepository = carMaintenanceRepository;
        this.priceService = priceService;
    }

    public List<CarMaintenance> getAllCarMaintenanceOptions() {
        List<CarMaintenance> carMaintenanceList = carMaintenanceRepository.getAll();
        return carMaintenanceList;
    }

    @Override
    public CarMaintenance getCarMaintenanceById(Long carMaintenanceId) {

        return carMaintenanceRepository.getById(carMaintenanceId);
    }
    @Override
    public CarMaintenance getCarMaintenanceByName(String carMaintenanceName) {
        return carMaintenanceRepository.getByField("carMaintenanceName", carMaintenanceName);
    }
    @Override
    public List<CarMaintenance> getAllCarMaintenances(Optional<String> search) {
        return carMaintenanceRepository.getAllCarMaintenances(search);
    }

    @Override
    public List<CarMaintenance> getAllCarMaintenanceFilter(CarMaintenanceFilterOptions carMaintenanceFilterOptions) {
        return carMaintenanceRepository.getAllCarMaintenanceFilter(carMaintenanceFilterOptions);
    }
    @Override
    public void deleteCarMaintenance(long carMaintenanceId, User user) {
        checkModifyPermissions(user);
        carMaintenanceRepository.delete(carMaintenanceId);
    }

    @Override
    public CarMaintenance createCarMaintenance(CarMaintenance carMaintenance) {

        carMaintenanceRepository.create(carMaintenance);
        return carMaintenance;
    }
    @Override
    public CarMaintenance updateCarMaintenance(CarMaintenance carMaintenance) {
        carMaintenanceRepository.update(carMaintenance);
        return carMaintenance;
    }

    @Override
    public List<CarMaintenance> getAllCarMaintenancesByUsername(String username) {
        return carMaintenanceRepository.getAllCarMaintenancesByUsername(username);
    }

    @Override
    public List<CarMaintenance> getAllCarMaintenancesByUserId(Long userId){
        return carMaintenanceRepository.getAllCarMaintenancesByUserId(userId);
    };
    private void checkModifyPermissions(User user) {
        String str = "admin";
        //TODO to check why throw exc while equals is applied
        if (!(user.getRole().getRoleName().equals(str))) {
            throw new AuthorizationException(MODIFY_BRAND_ERROR_MESSAGE);
        }
    }

}

package com.example.smart_garage.services;

import com.example.smart_garage.models.Brand;
import com.example.smart_garage.models.CarMaintenance;
import com.example.smart_garage.repositories.contracts.CarMaintenanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.Helpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarMaintenanceServiceImplTest {
    @Mock
    private CarMaintenanceRepository carMaintenanceRepository;

    @InjectMocks
    private CarMaintenanceServiceImpl carMaintenanceService;

    @Test
    void getAllCarMaintenances_Should_ReturnListOfObjects() {
        List<CarMaintenance> carMaintenances = new ArrayList<>();
        carMaintenances.add(createMockCarMaintenance());
        carMaintenances.add(createAnotherMockCarMaintenance());

        when(carMaintenanceRepository.getAll()).thenReturn(carMaintenances);
        List<CarMaintenance> result = carMaintenanceService.getAllCarMaintenanceOptions();

        assertEquals(carMaintenances.size(), result.size());
        verify(carMaintenanceRepository, Mockito.times(1)).getAll();

    }

    @Test
    void getCarMaintenanceById_Should_ReturnObject() {
        CarMaintenance mockCM = createMockCarMaintenance();

        when(carMaintenanceRepository.getById(mockCM.getCarMaintenanceId())).thenReturn(mockCM);

        CarMaintenance result = carMaintenanceService.getCarMaintenanceById(mockCM.getCarMaintenanceId());

        assertEquals(mockCM.getCarMaintenanceId(), result.getCarMaintenanceId());
        verify(carMaintenanceRepository, Mockito.times(1)).getById(mockCM.getCarMaintenanceId());
    }

    @Test
    void getCarMaintenanceByName_Should_ReturnObject() {
        List<CarMaintenance> listMockCMs = new ArrayList<>();
        listMockCMs.add(createMockCarMaintenance());
        Optional<String> search = Optional.of("name");

        when(carMaintenanceRepository.getAllCarMaintenances(search)).thenReturn(listMockCMs);

        List<CarMaintenance> result = carMaintenanceService.getAllCarMaintenances(search);

        assertEquals(listMockCMs.size(), result.size());
        verify(carMaintenanceRepository, Mockito.times(1)).getAllCarMaintenances(search);
    }

    @Test
    void deleteCarMaintenance() {
    }

    @Test
    void createCarMaintenance() {
        carMaintenanceService.createCarMaintenance(createMockCarMaintenance());
        Optional<CarMaintenance> mockService = Optional.of(createMockCarMaintenance());

        // Assert
        assertTrue(mockService.isPresent());
    }


}
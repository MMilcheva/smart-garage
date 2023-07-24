package com.example.smart_garage.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CarMaintenanceSaveRequest {
    @NotNull
    @Column(name = "car_maintenance_name")
    @Size(min = 2, max = 32, message = "Car Maintenance name must be between 2 and 32 symbols")
    private String carMaintenanceName;

    private Boolean isArchived = false;

    public CarMaintenanceSaveRequest() {
    }

    public String getCarMaintenanceName() {
        return carMaintenanceName;
    }

    public void setCarMaintenanceName(String carMaintenanceName) {
        this.carMaintenanceName = carMaintenanceName;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }
}

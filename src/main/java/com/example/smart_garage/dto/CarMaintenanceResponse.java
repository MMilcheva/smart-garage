package com.example.smart_garage.dto;

public class CarMaintenanceResponse {

    private String carMaintenanceName;
    private boolean isArchived;

    public CarMaintenanceResponse() {
    }

    public String getCarMaintenanceName() {
        return carMaintenanceName;
    }

    public void setCarMaintenanceName(String carMaintenanceName) {
        this.carMaintenanceName = carMaintenanceName;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}

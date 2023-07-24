package com.example.smart_garage.dto;

import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class CarMaintenanceFilterDto {


    private String carMaintenanceName;
    private String username;
    Double carMaintenanceListPrice;
    private Boolean isArchived;
    private String sortBy;
    private String sortOrder;

    public CarMaintenanceFilterDto() {
    }

    public String getCarMaintenanceName() {
        return carMaintenanceName;
    }

    public void setCarMaintenanceName(String carMaintenanceName) {
        this.carMaintenanceName = carMaintenanceName;
    }

    public Double getCarMaintenanceListPrice() {
        return carMaintenanceListPrice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}

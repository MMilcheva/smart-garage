package com.example.smart_garage.models;

import org.springframework.stereotype.Component;

import java.util.Optional;


public class CarMaintenanceFilterOptions {

    private Optional<String> carMaintenanceName;
    private Optional<Double> carMaintenanceListPrice;
    private Optional<String> username;
    private Optional<Boolean> isArchived;

    private Optional<String> sortBy;

    private Optional<String> sortOrder;

    public CarMaintenanceFilterOptions(String carMaintenanceName, Double carMaintenanceListPrice, String username, Boolean isArchived, String sortBy, String sortOrder) {
        this.carMaintenanceName = Optional.ofNullable(carMaintenanceName);
        this.carMaintenanceListPrice = Optional.ofNullable(carMaintenanceListPrice);
        this.username = Optional.ofNullable(username);
        this.isArchived = Optional.ofNullable(isArchived);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public CarMaintenanceFilterOptions() {
    }

    public Optional<String> getCarMaintenanceName() {
        return carMaintenanceName;
    }

    public void setCarMaintenanceName(Optional<String> carMaintenanceName) {
        this.carMaintenanceName = carMaintenanceName;
    }

    public Optional<Double> getCarMaintenanceListPrice() {
        return carMaintenanceListPrice;
    }

    public void setCarMaintenanceListPrice(Optional<Double> carMaintenanceListPrice) {
        this.carMaintenanceListPrice = carMaintenanceListPrice;
    }

    public Optional<Boolean> getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Optional<Boolean> isArchived) {
        this.isArchived = isArchived;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public void setUsername(Optional<String> username) {
        this.username = username;
    }

    public Optional<Boolean> getArchived() {
        return isArchived;
    }

    public void setArchived(Optional<Boolean> isArchived) {
        this.isArchived = isArchived;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public void setSortBy(Optional<String> sortBy) {
        this.sortBy = sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }
}
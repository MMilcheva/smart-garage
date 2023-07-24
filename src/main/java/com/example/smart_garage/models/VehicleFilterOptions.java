package com.example.smart_garage.models;

import java.util.Optional;

public class VehicleFilterOptions {

    private Optional<Long> vehicleId;
    private Optional<Long> userId;
    private Optional<String> plate;
    private Optional<String> vin;
    private Optional<Integer> yearOfCreation;
    private Optional<String> modelName;
    private Optional<String> username;
    private Optional<String> ownerFirstName;
    private Optional<String> ownerLastName;
    private Optional<Boolean> isArchived;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public VehicleFilterOptions(Long vehicleId,
                                Long userId,
                                String plate,
                                String vin,
                                Integer yearOfCreation,
                                String modelName,
                                String username,
                                String ownerFirstName,
                                String ownerLastName,
                                Boolean isArchived,
                                String sortBy,
                                String sortOrder) {
        this.vehicleId = Optional.ofNullable(vehicleId);
        this.userId = Optional.ofNullable(userId);
        this.plate = Optional.ofNullable(plate);
        this.vin = Optional.ofNullable(vin);
        this.yearOfCreation = Optional.ofNullable(yearOfCreation);
        this.modelName = Optional.ofNullable(modelName);
        this.username = Optional.ofNullable(username);
        this.ownerFirstName = Optional.ofNullable(ownerFirstName);
        this.ownerLastName = Optional.ofNullable(ownerLastName);
        this.isArchived=Optional.ofNullable(isArchived);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<Long> getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Optional<Long> vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Optional<String> getPlate() {
        return plate;
    }

    public void setPlate(Optional<String> plate) {
        this.plate = plate;
    }

    public Optional<String> getVin() {
        return vin;
    }

    public void setVin(Optional<String> vin) {
        this.vin = vin;
    }

    public Optional<Integer> getYearOfCreation() {
        return yearOfCreation;
    }

    public void setYearOfCreation(Optional<Integer> yearOfCreation) {
        this.yearOfCreation = yearOfCreation;
    }

    public Optional<String> getModelName() {
        return modelName;
    }

    public void setModelName(Optional<String> modelName) {
        this.modelName = modelName;
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

    public Optional<String> getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(Optional<String> ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public Optional<String> getOwnerLatName() {
        return ownerLastName;
    }

    public void setOwnerLatName(Optional<String> ownerLatName) {
        this.ownerLastName = ownerLatName;
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

    public Optional<Long> getUserId() {
        return userId;
    }

    public void setUserId(Optional<Long> userId) {
        this.userId = userId;
    }

    public Optional<String> getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(Optional<String> ownerLastName) {
        this.ownerLastName = ownerLastName;
    }
}

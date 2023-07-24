package com.example.smart_garage.models;

import java.util.Optional;

public class CarModelFilterOptions {

    private Optional<Long> carModelId;
    private Optional<String> carModelName;
    private Optional<String> brandName;
    private Optional<Boolean> isArchived;

    private Optional<String> sortBy;

    private Optional<String> sortOrder;

    private boolean combineResults;

    public CarModelFilterOptions(Long carModelId, String carModelName, String brandName, Boolean isArchived, String sortBy, String sortOrder) {
        this.carModelId = Optional.ofNullable(carModelId);
        this.brandName = Optional.ofNullable(brandName);
        this.carModelName = Optional.ofNullable(carModelName);
        this.isArchived = Optional.ofNullable(isArchived);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
        this.combineResults = true;
    }

    public Optional<Long> getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(Optional<Long> carModelId) {
        this.carModelId = carModelId;
    }

    public Optional<String> getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(Optional<String> carModelName) {
        this.carModelName = carModelName;
    }

    public Optional<String> getBrandName() {
        return brandName;
    }

    public void setBrandName(Optional<String> brandName) {
        this.brandName = brandName;
    }

    public Optional<Boolean> getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Optional<Boolean> isArchived) {
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

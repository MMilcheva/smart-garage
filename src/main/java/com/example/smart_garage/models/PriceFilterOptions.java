package com.example.smart_garage.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Optional;

public class PriceFilterOptions {

    private Optional<Double> amount;
      @DateTimeFormat(pattern = "dd-MMM-yyyy")
    private Optional<LocalDate> validOn;
    private Optional<Long> carMaintenanceId;
    private Optional<String> carMaintenanceName;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public PriceFilterOptions() {
        this(null, null, null, null, null, null);
    }

    public PriceFilterOptions(Double amount, LocalDate validOn, String carMaintenanceName, Long carMaintenanceId, String sortBy,
                              String sortOrder) {
        this.amount = Optional.ofNullable(amount);
        this.validOn = Optional.ofNullable(validOn);
        this.carMaintenanceId = Optional.ofNullable(carMaintenanceId);
        this.carMaintenanceName = Optional.ofNullable(carMaintenanceName);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }


    public Optional<Double> getAmount() {
        return amount;
    }

    public void setAmount(Optional<Double> amount) {
        this.amount = amount;
    }

    public Optional<LocalDate> getValidOn() {
        return validOn;
    }

    public void setValidOn(Optional<LocalDate> validOn) {
        this.validOn = validOn;
    }

    public Optional<String> getCarMaintenanceName() {
        return carMaintenanceName;
    }

    public void setCarMaintenanceName(Optional<String> carMaintenanceName) {
        this.carMaintenanceName = carMaintenanceName;
    }

    public Optional<Long> getCarMaintenanceId() {
        return carMaintenanceId;
    }

    public void setCarMaintenanceId(Optional<Long> carMaintenanceId) {
        this.carMaintenanceId = carMaintenanceId;
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

package com.example.smart_garage.dto;

import com.example.smart_garage.models.CarMaintenance;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.util.Optional;

public class PriceFilterDto {
    private Double amount;
    @DateTimeFormat(pattern = "dd-MMM-yyyy")
    private LocalDate validOn;
    private Long carMaintenanceId;
    private String carMaintenanceName;
    private String sortBy;
    private String sortOrder;

    public PriceFilterDto() {
    }

    public LocalDate getValidOn() {
        return validOn;
    }

    public void setValidOn(LocalDate validOn) {
        this.validOn = validOn;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCarMaintenanceId() {
        return carMaintenanceId;
    }

    public void setCarMaintenanceId(Long carMaintenanceId) {
        this.carMaintenanceId = carMaintenanceId;
    }

    public String getCarMaintenanceName() {
        return carMaintenanceName;
    }

    public void setCarMaintenanceName(String carMaintenanceName) {
        this.carMaintenanceName = carMaintenanceName;
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

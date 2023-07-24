package com.example.smart_garage.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public class PriceSaveRequest {
     private Long carMaintenanceId;
    @NotNull
    @PositiveOrZero
    private Double amount;

    private LocalDate validFrom;

    private LocalDate validTo;

    public PriceSaveRequest() {
    }

    public Long getCarMaintenanceId() {
        return carMaintenanceId;
    }

    public void setCarMaintenanceId(Long carMaintenanceId) {
        this.carMaintenanceId = carMaintenanceId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
}

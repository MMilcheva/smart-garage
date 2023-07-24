package com.example.smart_garage.dto;

import com.example.smart_garage.enumeration.Currency;
import com.example.smart_garage.models.CarMaintenance;
import com.example.smart_garage.models.Price;
import com.example.smart_garage.models.Visit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;

public class OrderSaveRequest {
    private Long visitId;
    private String carMaintenanceName;
    private Long carMaintenanceID;
    private String currencyCode;

    public OrderSaveRequest() {
    }

    public Long getCarMaintenanceID() {
        return carMaintenanceID;
    }

    public void setCarMaintenanceID(Long carMaintenanceID) {
        this.carMaintenanceID = carMaintenanceID;
    }

    public String getCarMaintenanceName() {
        return carMaintenanceName;
    }

    public void setCarMaintenanceName(String carMaintenanceName) {
        this.carMaintenanceName = carMaintenanceName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }
}

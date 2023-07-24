package com.example.smart_garage.dto;

import com.example.smart_garage.enumeration.Currency;
import com.example.smart_garage.models.CarMaintenance;
import com.example.smart_garage.models.Price;
import com.example.smart_garage.models.Visit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

public class OrderResponse {

    private Long orderId;

    private LocalDate dateOfCreation;

    private CarMaintenance carMaintenance;


    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.BGN;

    private Visit visit;

    private Price price;

    public OrderResponse() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public CarMaintenance getCarMaintenance() {
        return carMaintenance;
    }

    public void setCarMaintenance(CarMaintenance carMaintenance) {
        this.carMaintenance = carMaintenance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}

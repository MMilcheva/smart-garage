package com.example.smart_garage.models;

import java.time.LocalDate;
import java.util.Optional;

public class OrderFilterOptions {


    private Optional<Long> orderId;
    private Optional<Long> userId;
    private Optional<String> orderName;
    private Optional<LocalDate> dateOfCreation;
    private Optional<String> carMaintenanceName;
    private Optional<String> currencyCode;
    private Optional<Long> visitId;
    private Optional<String> plate;

    private Optional<Double> priceAmount;

    private Optional<String> sortBy;

    private Optional<String> sortOrder;

    public OrderFilterOptions() {
    }

    public OrderFilterOptions(Long orderId, Long userId, String orderName, LocalDate dateOfCreation, String carMaintenanceName, String currencyCode, Long visitId, String plate, Double priceAmount, String sortBy, String sortOrder) {
        this.orderId = Optional.ofNullable(orderId);
        this.userId = Optional.ofNullable(userId);
        this.orderName = Optional.ofNullable(orderName);
        this.dateOfCreation = Optional.ofNullable(dateOfCreation);
        this.carMaintenanceName = Optional.ofNullable(carMaintenanceName);
        this.currencyCode = Optional.ofNullable(currencyCode);
        this.visitId = Optional.ofNullable(visitId);
        this.plate = Optional.ofNullable(plate);
        this.priceAmount = Optional.ofNullable(priceAmount);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<Long> getUserId() {
        return userId;
    }

    public void setUserId(Optional<Long> userId) {
        this.userId = userId;
    }

    public Optional<String> getOrderName() {
        return orderName;
    }

    public void setOrderName(Optional<String> orderName) {
        this.orderName = orderName;
    }

    public Optional<LocalDate> getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Optional<LocalDate> dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Optional<String> getCarMaintenanceName() {
        return carMaintenanceName;
    }

    public void setCarMaintenanceName(Optional<String> carMaintenanceName) {
        this.carMaintenanceName = carMaintenanceName;
    }

    public Optional<String> getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(Optional<String> currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Optional<Long> getVisitId() {
        return visitId;
    }

    public void setVisitId(Optional<Long> visitId) {
        this.visitId = visitId;
    }

    public Optional<Long> getOrderId() {
        return orderId;
    }

    public void setOrderId(Optional<Long> orderId) {
        this.orderId = orderId;
    }

    public Optional<String> getPlate() {
        return plate;
    }

    public void setPlate(Optional<String> plate) {
        this.plate = plate;
    }

    public Optional<Double> getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(Optional<Double> priceAmount) {
        this.priceAmount = priceAmount;
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

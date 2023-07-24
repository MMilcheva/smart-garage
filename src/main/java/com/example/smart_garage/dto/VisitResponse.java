package com.example.smart_garage.dto;

import com.example.smart_garage.models.Vehicle;

import com.example.smart_garage.models.User;

import java.time.LocalDate;

public class VisitResponse {

    private Long visitId;
    private LocalDate startDateOfVisit;
    private Vehicle vehicle;
    private User user;
    private LocalDate endDateOfVisit;
    private boolean isArchived;

    private Long paymentId;
    private String notes;
    private String orders;


    public VisitResponse() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }

    public LocalDate getStartDateOfVisit() {
        return startDateOfVisit;
    }

    public void setStartDateOfVisit(LocalDate startDateOfVisit) {
        this.startDateOfVisit = startDateOfVisit;
    }

   public LocalDate getEndDateOfVisit() {
        return endDateOfVisit;
    }

    public void setEndDateOfVisit(LocalDate endDateOfVisit) {
        this.endDateOfVisit = endDateOfVisit;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}

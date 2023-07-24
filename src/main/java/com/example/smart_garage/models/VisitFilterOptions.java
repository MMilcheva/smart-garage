package com.example.smart_garage.models;

import com.example.smart_garage.enumeration.PaymentStatus;

import java.time.LocalDate;
import java.util.Optional;

public class VisitFilterOptions {

    private Optional<Long> visitId;
    private Optional<Long> userId;
    private Optional<LocalDate> minDate;
    private Optional<LocalDate> maxDate;
    private Optional<String> plate;
    private Optional<PaymentStatus> paymentStatus;
    private Optional<String> notes;
    private Optional<Boolean> isArchived;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public VisitFilterOptions(Long visitId,
                              Long userId,
                              LocalDate minDate,
                              LocalDate maxDate,
                              String plate,
                              PaymentStatus paymentStatus,
                              String notes,
                              Boolean isArchived,
                              String sortBy,
                              String sortOrder) {
        this.visitId = Optional.ofNullable(visitId);
        this.userId = Optional.ofNullable(userId);
        this.minDate = Optional.ofNullable(minDate);
        this.maxDate = Optional.ofNullable(maxDate);
        this.plate = Optional.ofNullable(plate);
        this.paymentStatus = Optional.ofNullable(paymentStatus);
        this.notes = Optional.ofNullable(notes);
        this.isArchived = Optional.ofNullable(isArchived);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<Boolean> getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Optional<Boolean> isArchived) {
        this.isArchived = isArchived;
    }

    public Optional<Long> getUserId() {
        return userId;
    }

    public void setUserId(Optional<Long> userId) {
        this.userId = userId;
    }


    public Optional<Long> getVisitId() {
        return visitId;
    }

    public void setVisitId(Optional<Long> visitId) {
        this.visitId = visitId;
    }

    public Optional<LocalDate> getMinDate() {
        return minDate;
    }

    public void setMinDate(Optional<LocalDate> minDate) {
        this.minDate = minDate;
    }

    public Optional<LocalDate> getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Optional<LocalDate> maxDate) {
        this.maxDate = maxDate;
    }

    public Optional<String> getPlate() {
        return plate;
    }

    public void setPlate(Optional<String> plate) {
        this.plate = plate;
    }

    public Optional<PaymentStatus> getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Optional<PaymentStatus> paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Optional<String> getNotes() {
        return notes;
    }

    public void setNotes(Optional<String> notes) {
        this.notes = notes;
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

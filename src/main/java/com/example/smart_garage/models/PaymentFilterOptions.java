package com.example.smart_garage.models;

import com.example.smart_garage.enumeration.Currency;
import com.example.smart_garage.enumeration.PaymentStatus;

import java.time.LocalDate;
import java.util.Optional;

public class PaymentFilterOptions {
    private Optional<Long> userId;
    private Optional<Double> listPrice;
    private Optional<Double> discount;
    private Optional<Double> vat;
    private Optional<Double> totalPriceBGN;
    private Optional<LocalDate> dateOfPayment;
    private Optional<PaymentStatus> paymentStatus;
    private Optional<Currency> selectedCurrency;
    private Optional<Double> exchangeRate;
    private Optional<Boolean> isArchived;
    private Optional<Long> visitId;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public PaymentFilterOptions() {
        this(null, null, null, null, null, null, null,
                null, null, null, null, null, null);
    }

    public PaymentFilterOptions(Long userId, Double listPrice, Double discount, Double vat, Double totalPriceBGN, LocalDate dateOfPayment,
                                PaymentStatus paymentStatus, Currency selectedCurrency, Double exchangeRate, Boolean isArchived,
                                Long visitId, String sortBy, String sortOrder) {
        this.userId = Optional.ofNullable(userId);
        this.listPrice = Optional.ofNullable(listPrice);
        this.discount = Optional.ofNullable(discount);
        this.vat = Optional.ofNullable(vat);
        this.totalPriceBGN = Optional.ofNullable(totalPriceBGN);
        this.dateOfPayment = Optional.ofNullable(dateOfPayment);
        this.paymentStatus = Optional.ofNullable(paymentStatus);
        this.selectedCurrency = Optional.ofNullable(selectedCurrency);
        this.exchangeRate = Optional.ofNullable(exchangeRate);
        this.isArchived = Optional.ofNullable(isArchived);
        this.visitId = Optional.ofNullable(visitId);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<Long> getUserId() {
        return userId;
    }

    public void setUserId(Optional<Long> userId) {
        this.userId = userId;
    }

    public Optional<Double> getListPrice() {
        return listPrice;
    }

    public void setListPrice(Optional<Double> listPrice) {
        this.listPrice = listPrice;
    }

    public Optional<Double> getDiscount() {
        return discount;
    }

    public void setDiscount(Optional<Double> discount) {
        this.discount = discount;
    }

    public Optional<Double> getVat() {
        return vat;
    }

    public void setVat(Optional<Double> vat) {
        this.vat = vat;
    }

    public Optional<Double> getTotalPriceBGN() {
        return totalPriceBGN;
    }

    public void setTotalPriceBGN(Optional<Double> totalPriceBGN) {
        this.totalPriceBGN = totalPriceBGN;
    }

    public Optional<LocalDate> getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(Optional<LocalDate> dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public Optional<PaymentStatus> getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Optional<PaymentStatus> paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Optional<Currency> getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(Optional<Currency> selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public Optional<Double> getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Optional<Double> exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Optional<Boolean> getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Optional<Boolean> isArchived) {
        this.isArchived = isArchived;
    }

    public Optional<Long> getVisitId() {
        return visitId;
    }

    public void setVisitId(Optional<Long> visitId) {
        this.visitId = visitId;
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

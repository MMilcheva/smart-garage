package com.example.smart_garage.dto;

import com.example.smart_garage.enumeration.Currency;
import com.example.smart_garage.enumeration.PaymentStatus;
import com.example.smart_garage.models.Visit;


import java.time.LocalDate;

public class PaymentResponse {

    private Long paymentId;
    private Double listPrice;
    private Double discount;
    private Double vat;
    private Double totalPriceBGN;
    private Currency selectedCurrency;

    private Double exchangeRate;
    private Double listPriceSelectedCurrency;
    private Double discountSelectedCurrency;
    private Double vatSelectedCurrency;
    private Double totalPriceSelectedCurrency;
    private LocalDate dateOfPayment;
    private PaymentStatus paymentStatus;
    private Visit visit;
    public PaymentResponse() {
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Double getTotalPriceBGN() {
        return totalPriceBGN;
    }

    public void setTotalPriceBGN(Double totalPriceBGN) {
        this.totalPriceBGN = totalPriceBGN;
    }

    public LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(LocalDate dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public Double getListPrice() {
        return listPrice;
    }

    public void setListPrice(Double listPrice) {
        this.listPrice = listPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Currency getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(Currency selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getListPriceSelectedCurrency() {
        return listPriceSelectedCurrency;
    }

    public void setListPriceSelectedCurrency(Double listPriceSelectedCurrency) {
        this.listPriceSelectedCurrency = listPriceSelectedCurrency;
    }

    public Double getDiscountSelectedCurrency() {
        return discountSelectedCurrency;
    }

    public void setDiscountSelectedCurrency(Double discountSelectedCurrency) {
        this.discountSelectedCurrency = discountSelectedCurrency;
    }

    public Double getVatSelectedCurrency() {
        return vatSelectedCurrency;
    }

    public void setVatSelectedCurrency(Double vatSelectedCurrency) {
        this.vatSelectedCurrency = vatSelectedCurrency;
    }

    public Double getTotalPriceSelectedCurrency() {
        return totalPriceSelectedCurrency;
    }

    public void setTotalPriceSelectedCurrency(Double totalPriceSelectedCurrency) {
        this.totalPriceSelectedCurrency = totalPriceSelectedCurrency;
    }
}

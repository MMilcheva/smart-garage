package com.example.smart_garage.dto;

import com.example.smart_garage.enumeration.Currency;
import com.example.smart_garage.enumeration.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


import java.time.LocalDate;

public class PaymentSaveRequest {

    private Long visitId;
    private LocalDate dateOfPayment;
    private double amountBGN;

    @Enumerated(EnumType.STRING)
    private Currency selectedCurrency;

    private PaymentStatus paymentStatus;

    private Double discount;

    private Double vat;


    public PaymentSaveRequest() {
    }

    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }

    public Currency getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(Currency selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }


    public LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(LocalDate dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public double getAmountBGN() {
        return amountBGN;
    }

    public void setAmountBGN(double amountBGN) {
        this.amountBGN = amountBGN;
    }

    public Double getDiscount() {
        return discount;
    }
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }
}

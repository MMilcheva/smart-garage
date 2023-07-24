package com.example.smart_garage.models;

import com.example.smart_garage.enumeration.Currency;
import com.example.smart_garage.enumeration.PaymentStatus;
import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "list_price")
    private Double listPrice;
    @Column(name = "discount")
    private Double discount;
    @Column(name = "VAT")
    private Double vat;
    @Column(name = "total_price_BGN")
    private Double totalPriceBGN;
    @Column(name = "date_of_payment")
    private LocalDate dateOfPayment;
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Column(name = "original_currency")
    @Enumerated(EnumType.STRING)
    private Currency selectedCurrency;
    @Column(name = "exchange_rate")
    private Double exchangeRate;


    @Column(name = "isArchived")
    private boolean isArchived;
    @OneToOne
    @JoinColumn(name = "visit_id")
    private Visit visit;

    public Payment() {
    }

    public Currency getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(Currency selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
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

       public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
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

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}

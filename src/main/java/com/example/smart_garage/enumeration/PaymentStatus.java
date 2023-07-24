package com.example.smart_garage.enumeration;

public enum PaymentStatus {
    PAID("PAID"),
    UNPAID("UNPAID");


    private final String paymentStatusValue;

    private PaymentStatus(String paymentStatusValue) {
        this.paymentStatusValue = paymentStatusValue;
    }

    public String getPaymentStatusValue() {
        return paymentStatusValue;
    }
}


package com.example.smart_garage.enumeration;

public enum Currency {
    BGN("BGN"),
    EUR("EUR"),
    USD("USD"),
    GBR("GBR"),
    CHF("CHF");

    public final String getValue;

    private Currency(String value) {
        this.getValue = value;
    }
}

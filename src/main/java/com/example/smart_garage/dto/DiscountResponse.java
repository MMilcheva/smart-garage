package com.example.smart_garage.dto;

import com.example.smart_garage.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class DiscountResponse {

    private String discountName;
    private User user;

    private Double discountAmount;

    private LocalDate validFrom;

    private LocalDate validTo;

    public DiscountResponse() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }
}

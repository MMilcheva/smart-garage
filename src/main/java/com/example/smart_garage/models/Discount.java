package com.example.smart_garage.models;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long discountId;
    @Column(name = "discount_name")
    private String discountName = "Discount for loyalty";
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
     @Column(name = "discount_amount")
    private Double discountAmount;
    @Column(name = "valid_from")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate validFrom;
    @Column(name = "valid_to")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate validTo;

    @Column(name = "isArchived")
    private Boolean isArchived = false;
    public Discount() {
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
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

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }
}

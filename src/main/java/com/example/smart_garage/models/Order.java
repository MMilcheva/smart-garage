package com.example.smart_garage.models;

import com.example.smart_garage.enumeration.Currency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_name")
    private String orderName = "Order";
    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation = LocalDate.now();
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "car_maintenance_id")
    private CarMaintenance carMaintenance;

    @Transient
    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.BGN;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "visit_id")
    private Visit visit;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "price_id")
    private Price price;

    public Order() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public CarMaintenance getCarMaintenance() {
        return carMaintenance;
    }

    public void setCarMaintenance(CarMaintenance carMaintenance) {
        this.carMaintenance = carMaintenance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}

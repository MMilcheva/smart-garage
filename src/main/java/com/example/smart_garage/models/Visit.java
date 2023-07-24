package com.example.smart_garage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "visits")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private Long visitId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date_of_visit")
    private LocalDate startDateOfVisit;
    @Column(name = "end_date_of_visit")
    private LocalDate endDateOfVisit;

    @Column(name = "notes")
    private String notes;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @Column(name = "isArchived")
    private Boolean isArchived;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, columnDefinition = "int default 0")
    private Payment payment;
    @JsonIgnore
    @OneToMany(mappedBy = "visit", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Order> orders;

    public Visit() {

    }

    public Visit(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.payment = new Payment();
    }

    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }

    public LocalDate getStartDateOfVisit() {
        return startDateOfVisit;
    }

    public void setStartDateOfVisit(LocalDate startDateOfVisit) {
        this.startDateOfVisit = startDateOfVisit;
    }

    public LocalDate getEndDateOfVisit() {
        return endDateOfVisit;
    }

    public void setEndDateOfVisit(LocalDate endDateOfVisit) {
        this.endDateOfVisit = endDateOfVisit;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


}

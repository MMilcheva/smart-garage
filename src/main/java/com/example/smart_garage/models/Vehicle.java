package com.example.smart_garage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id", insertable = false)
    private Long vehicleId;

    //e.g.: CA1088MM
    @NotNull(message = "license plate cannot be empty")
    @Column(name = "plate")
    @Size(min = 7, max = 8, message = "license plate has to be between 7 and 8 symbols!")
    private String plate;

    //e.g.: 123456789101112131
    @NotNull(message = "VIN cannot be empty")
    @Size(min = 17, max = 17, message = "VIN has to be exactly 17 chars. Numbers and letters only!")
    @Column(name = "vin", length = 17)
    private String vin;
    @NotNull(message = "creation year cannot be empty")
    @Min(value = 1886, message = "Year of creation must be equal or greater than 1886")
    @Max(9999)
    @Column(name = "year_of_creation", length = 4)
    private int yearOfCreation;

    //    @ManyToOne
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_id")
    private CarModel carModel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "isArchived")
    private Boolean isArchived = false;
    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Visit> visits;

    public Vehicle() {
    }

    public Vehicle(Long vehicleId, String plate, String vin, int yearOfCreation, CarModel carModel, User user,
                   boolean isArchived, Set<Visit> visits) {
        this.vehicleId = vehicleId;
        this.plate = plate;
        this.vin = vin;
        this.yearOfCreation = yearOfCreation;
        this.carModel = carModel;
        this.user = user;
        this.isArchived = isArchived;
        this.visits = visits;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getYearOfCreation() {
        return yearOfCreation;
    }

    public void setYearOfCreation(int yearOfCreation) {
        this.yearOfCreation = yearOfCreation;
    }

    public CarModel getModel() {
        return carModel;
    }

    public void setModel(CarModel model) {
        this.carModel = model;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public void setVisits(Set<Visit> visits) {
        this.visits = visits;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(vehicleId, vehicle.vehicleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId);
    }

    public Boolean isArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }
}

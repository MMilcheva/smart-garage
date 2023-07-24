package com.example.smart_garage.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;


public class VehicleSaveRequest {
    @NotNull(message = "license plate cannot be empty")
    @Column(name = "plate")
    @Size(min = 7, max = 8, message = "license plate has to be between 7 and 8 symbols!")
    private String plate;

    @NotNull(message = "VIN cannot be empty")
    @Size(min = 17, max = 17, message = "VIN has to be exactly 17 chars. Numbers and letters only!")
    @Column(name = "vin", length = 17)
    private String vin;
    @NotNull(message = "creation year cannot be empty")
    @Max(9999)
    @Min(value = 1886, message = "Year of creation must be equal or greater than 1886")
    private int yearOfCreation;

    @Size(min = 2, max = 32, message = "Model name must be between 2 and 32 symbols")
    private String carModelName;

    @Email
    private String email;

    private Boolean isArchived = false;

    public VehicleSaveRequest() {
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
//        if (yearOfCreation < 1886) {
//            throw new IllegalArgumentException("Year must be equal or greater than 1886");
//        }
        this.yearOfCreation = yearOfCreation;
    }

    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }
}

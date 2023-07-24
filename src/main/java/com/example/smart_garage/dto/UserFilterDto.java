package com.example.smart_garage.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserFilterDto {

    private String username;
    private Integer visitId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate minDate;
    private LocalDate maxDate;
    private String plate;
    //TODO fix vehilce to become plate
    private String roleName;
    private String sortBy;
    private String sortOrder;

    public UserFilterDto() {
    }

    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public String getMinDateAsString(){
        return formatDate(minDate);
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public String getMaxDateAsString(){
        return formatDate(maxDate);
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

//    public String getVehicle() {
//        return plate;
//    }
//
//    public void setVehicle(String vehicle) {
//        this.plate = vehicle;
//    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}

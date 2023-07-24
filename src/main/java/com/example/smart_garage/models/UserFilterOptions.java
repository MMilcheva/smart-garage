package com.example.smart_garage.models;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.util.ArrayList;



public class UserFilterOptions {
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> email;
    private Optional<String> username;
    private Optional<String> phoneNumber;
    private Optional<LocalDate> minDate;
    private Optional<LocalDate> maxDate;
    private Optional<String> plate;
    private Optional<String> roleDescription;
    private Optional<Integer> visitId;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public UserFilterOptions() {
        this(null, null,null, null, null, null,
                null, null, null, null, null, null);
    }

    public UserFilterOptions(String firstName, String lastName, String email,String username, String phoneNumber, LocalDate minDate,
                             LocalDate maxDate, String plate, String roleDescription, Integer visitId,
                             String sortBy, String sortOrder) {
        this.username = Optional.ofNullable(username);
        this.visitId = Optional.ofNullable(visitId);
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.email = Optional.ofNullable(email);
        this.phoneNumber = Optional.ofNullable(phoneNumber);
        this.minDate = Optional.ofNullable(minDate);
        this.maxDate = Optional.ofNullable(maxDate);
        this.plate = Optional.ofNullable(plate);
        this.roleDescription = Optional.ofNullable(roleDescription);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getUsername() {
        return username;
    }

    public void setUsername(Optional<String> username) {
        this.username = username;
    }

    public Optional<Integer> getVisitId() {
        return visitId;
    }

    public void setVisitId(Optional<Integer> visitId) {
        this.visitId = visitId;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(Optional<String> firstName) {
        this.firstName = firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public void setLastName(Optional<String> lastName) {
        this.lastName = lastName;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Optional<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Optional<LocalDate> getMinDate() {
        return minDate;
    }

    public void setMinDate(Optional<LocalDate> minDate) {
        this.minDate = minDate;
    }

    public Optional<LocalDate> getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Optional<LocalDate> maxDate) {
        this.maxDate = maxDate;
    }

    public Optional<String> getVehicle() {
        return plate;
    }

    public void setVehicle(Optional<String> vehicle) {
        this.plate = vehicle;
    }

    public Optional<String> getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(Optional<String> roleDescription) {
        this.roleDescription = roleDescription;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public void setSortBy(Optional<String> sortBy) {
        this.sortBy = sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }


}


package com.example.smart_garage.dto;

import java.time.LocalDate;

public class VisitSaveRequest {

    private String plate;
    private String notes;
    private Boolean isArchived;
    private LocalDate endDateOfVisit;

    public VisitSaveRequest() {
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public LocalDate getEndDateOfVisit() {
        return endDateOfVisit;
    }

    public void setEndDateOfVisit(LocalDate endDateOfVisit) {
        this.endDateOfVisit = endDateOfVisit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}

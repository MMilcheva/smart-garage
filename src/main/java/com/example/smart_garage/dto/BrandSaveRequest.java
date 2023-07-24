package com.example.smart_garage.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BrandSaveRequest {

    @NotNull
    @Column(name = "brand_name")
    @Size(min = 2, max = 32, message = "Brand name must be between 2 and 32 symbols")
    private String brandName;
    private boolean archived;

    public BrandSaveRequest() {
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}

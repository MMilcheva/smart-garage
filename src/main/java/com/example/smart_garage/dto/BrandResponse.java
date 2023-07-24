package com.example.smart_garage.dto;

public class BrandResponse {

    private String brandName;
    private boolean isArchived;

    public BrandResponse() {
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(boolean archived) {
        isArchived = archived;
    }
}

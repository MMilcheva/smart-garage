package com.example.smart_garage.dto;

import com.example.smart_garage.models.Brand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CarModelSaveRequest {
    @NotNull
    @Size(min = 2, max = 32, message = "Model name must be between 2 and 32 symbols")
    private String carModelName;
    private Brand brand;
    private Boolean archived = false;

    private Long brandId;
    private String brandName;

    public CarModelSaveRequest() {
    }

    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}

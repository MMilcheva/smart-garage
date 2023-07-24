package com.example.smart_garage;

import com.example.smart_garage.models.Brand;

public class Helpers {

    public static Brand createMockBrand() {
        Brand brand = new Brand();
        brand.setBrandId(1L);
        brand.setBrandName("Tesla");
        return brand;
    }
}

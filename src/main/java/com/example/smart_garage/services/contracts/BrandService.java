package com.example.smart_garage.services.contracts;

import com.example.smart_garage.models.BrandFilterOptions;
import com.example.smart_garage.models.Brand;
import com.example.smart_garage.models.User;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    Brand getBrandById(Long brandToBeUpdatedId);

    Brand getBrandByName(String name);

    List<Brand> getAllBrands(Optional<String> search);

    List<Brand> getAllBrandOptions();

    List<Brand> getAllBrandsFilter(BrandFilterOptions brandFilterOptions);

    void deleteBrand(long brandId, User user);

    Brand createBrand(Brand brand);

    Brand checkIfBrandExists(Brand brand);

    Brand updateBrand(Brand brand);
}

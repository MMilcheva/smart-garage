package com.example.smart_garage.repositories.contracts;

import com.example.smart_garage.models.BrandFilterOptions;
import com.example.smart_garage.models.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends BaseCRUDRepository<Brand>{
    List<Brand> getAllBrands(Optional<String> search);

    List<Brand> getAllBrandsFilter(BrandFilterOptions brandFilterOptions);

    List<Brand> filter(Optional<String> brandName, Optional<Boolean> isArchived,
                       Optional<String> sortBy, Optional<String> sortOrder);

    Brand findBrandByName (String brandName);

    List<Brand> findByBrandName(String brandName);
}

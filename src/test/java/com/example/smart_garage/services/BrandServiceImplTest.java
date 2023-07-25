package com.example.smart_garage.services;

import com.example.smart_garage.models.Brand;
import com.example.smart_garage.repositories.contracts.BrandRepository;
import com.example.smart_garage.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.Helpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {
    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandService;

    @Test
    void getBrandById_Should_ReturnObject() {

        Brand brand = createMockBrand();

        Mockito.when(brandRepository.getById(brand.getBrandId())).thenReturn(brand);

        Optional<Brand> result = Optional.ofNullable(brandService.getBrandById(brand.getBrandId()));

        assertTrue(result.isPresent());
        assertEquals(brand.getBrandId(), result.get().getBrandId());
        Mockito.verify(brandRepository, Mockito.times(1)).getById(brand.getBrandId());
    }

    @Test
    void getBrandByName_Should_ReturnObject() {
        Brand brand = createMockBrand();

        Mockito.when(brandRepository.getByField("brandName", brand.getBrandName())).thenReturn(brand);

        Optional<Brand> result = Optional.ofNullable(brandService.getBrandByName(brand.getBrandName()));

        assertTrue(result.isPresent());
        assertEquals(brand.getBrandName(), result.get().getBrandName());
        Mockito.verify(brandRepository, Mockito.times(1)).getByField("brandName", brand.getBrandName());
    }

    @Test
    void getAllBrands_Should_ReturnListOfObjects() {
        List<Brand> brandList = new ArrayList<>();

        brandList.add(createMockBrand());
        brandList.add(createMockBrand());

        Optional<String> search = Optional.empty();
        Mockito.when(brandRepository.getAllBrands(search)).thenReturn(brandList);
        List<Brand> result = brandService.getAllBrands(search);

        assertEquals(brandList.size(), result.size());
        Mockito.verify(brandRepository, Mockito.times(1)).getAllBrands(search);


    }

    @Test
    void deleteBrand_Should_DeleteObject() {
        Brand brand = createMockBrand();

        brandService.deleteBrand(brand.getBrandId(), createMockUserAdmin());
        Mockito.verify(brandRepository).delete(brand.getBrandId());
    }

    @Test
    void createBrand_Should_ReturnCreatedObject() {
        Brand brand = createMockBrand();

        brandService.createBrand(brand);
        Mockito.verify(brandRepository).create(brand);
    }

    @Test
    void checkIfBrandExists() {
    }

    @Test
    void updateBrand() {
    }
}
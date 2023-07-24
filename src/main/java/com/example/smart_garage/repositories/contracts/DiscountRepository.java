package com.example.smart_garage.repositories.contracts;

import com.example.smart_garage.models.Discount;
import com.example.smart_garage.models.DiscountFilterOptions;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends BaseCRUDRepository<Discount> {

    List<Discount> getAllDiscountsFilter(DiscountFilterOptions discountFilterOptions);

    List<Discount> filter(Optional<String> discountName,
                          Optional<String> username,
                          Optional<Boolean> isArchived,
                          Optional<String> sortBy,
                          Optional<String> sortOrder);

    List<Discount> getAllDiscounts(Optional<String> search);

    Discount getDiscountByUserValidOn(Long userId, LocalDate validOn);

    List<Discount> getAllDiscountsByUserId(Long userId);
}

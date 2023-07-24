package com.example.smart_garage.services.contracts;

import com.example.smart_garage.models.Discount;
import com.example.smart_garage.models.DiscountFilterOptions;
import com.example.smart_garage.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiscountService {


    Discount getDiscountById(Long discountToBeUpdated);

    List<Discount> getAllDiscounts(Optional<String> search);

    Discount getDiscountByUserValidOn(Long userId, LocalDate validOn);

    List<Discount> getAllDiscountsByUserId(Long userId);

    List<Discount> getAllDiscountsFilter(DiscountFilterOptions discountFilterOptions);

    void deleteDiscount(long discountId, User user);

    Discount createDiscount(Discount discount, User user);

    Discount updateDiscount(Discount discount, User user);

    Discount archiveDiscount(Discount discount);
}

package com.example.smart_garage.services.contracts;

import com.example.smart_garage.models.Price;
import com.example.smart_garage.models.PriceFilterOptions;
import com.example.smart_garage.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceService {


    List<Price> getAllPrices(Optional<String> search);

    List<Price> getAllPricesFilter(PriceFilterOptions priceFilterOptions);

    Price getPriceById (Long id);

    Price getPriceByIdValidOn(Long priceId, LocalDate validOn);

    void updatePrice(Price price, User user);

    Price createPrice(Price price, User user);

    void deletePrice(Long priceId, User user);
}

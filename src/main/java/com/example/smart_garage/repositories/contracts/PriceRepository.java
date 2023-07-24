package com.example.smart_garage.repositories.contracts;


import com.example.smart_garage.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface PriceRepository extends BaseCRUDRepository<Price>{

    Price getPriceValidOn(Long carMaintenanceId, LocalDate validOn);
    List<Price> getPriceByCarMaintenanceId(Long carMaintenanceId);
    List<Price> getAllPrices(Optional<String> search);

    List<Price> getAllPricesFilter(PriceFilterOptions priceFilterOptions);
    List<Price> filter(
            Optional<Double> amount,
            Optional<LocalDate> validOn,
            Optional<String> carMaintenanceName,
            Optional<String> sortBy,
            Optional<String> sortOrder);
}


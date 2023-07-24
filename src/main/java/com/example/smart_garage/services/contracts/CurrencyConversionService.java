package com.example.smart_garage.services.contracts;

import java.io.IOException;
import java.time.LocalDate;

public interface CurrencyConversionService {
    Double getExchangerateOnDate(LocalDate date, String base, String symbols) throws IOException;
}

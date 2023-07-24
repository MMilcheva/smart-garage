package com.example.smart_garage.repositories.contracts;

import com.example.smart_garage.enumeration.PaymentStatus;
import com.example.smart_garage.models.Visit;
import com.example.smart_garage.models.VisitFilterOptions;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends BaseCRUDRepository<Visit> {

    List<Visit> getAllVisits(Optional<String> search);
    List<Visit> getAllVisitsByUserId(Long userId);
    List<Visit> getAllVisitsFilter(VisitFilterOptions visitFilterOptions);
    List<Visit> filter(Optional<Long> visitId,
                       Optional<Long> userId,
                       Optional<LocalDate> minDate,
                       Optional<LocalDate> maxDate,
                       Optional<String> plate,
                       Optional<PaymentStatus> paymentStatus,
                       Optional<String> notes,
                       Optional<Boolean> isArchived,
                       Optional<String> sortBy,
                       Optional<String> sortOrder);
}

package com.example.smart_garage.services.contracts;

import com.example.smart_garage.models.User;
import com.example.smart_garage.models.Visit;
import com.example.smart_garage.models.VisitFilterOptions;

import java.util.List;
import java.util.Optional;

public interface VisitService {
    List<Visit> getAllVisits(Optional<String> search);

    List<Visit> getAllVisits();

    List<Visit> getAllVisitsFilter(VisitFilterOptions visitFilterOptions);

    Visit createVisit(Visit visit);

    Visit updateVisit(Visit visit);

    Visit archive(Visit visit, User user);

    Visit getVisitById(Long visitId);


    List<Visit> getAllVisitsByUserId(Long userId);
}

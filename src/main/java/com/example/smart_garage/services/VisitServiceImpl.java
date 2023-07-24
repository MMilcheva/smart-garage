package com.example.smart_garage.services;

import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.models.Payment;
import com.example.smart_garage.models.User;
import com.example.smart_garage.models.Visit;
import com.example.smart_garage.models.VisitFilterOptions;
import com.example.smart_garage.repositories.contracts.VisitRepository;
import com.example.smart_garage.services.contracts.OrderService;
import com.example.smart_garage.services.contracts.PaymentService;
import com.example.smart_garage.services.contracts.UserService;
import com.example.smart_garage.services.contracts.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@Service
public class VisitServiceImpl implements VisitService {
    private final VisitRepository visitRepository;
    private final UserService userService;
    private final OrderService orderService;


    @Autowired
    public VisitServiceImpl(VisitRepository visitRepository, UserService userService, OrderService orderService) {
        this.visitRepository = visitRepository;
        this.userService = userService;
        this.orderService = orderService;

    }

    @Override
    public List<Visit> getAllVisits(Optional<String> search) {
        return visitRepository.getAllVisits(search);
    }

    @Override
    public List<Visit> getAllVisits() {
        return visitRepository.getAll();
    }

    @Override
    public List<Visit> getAllVisitsFilter(VisitFilterOptions visitFilterOptions) {
        return visitRepository.getAllVisitsFilter(visitFilterOptions);
    }

    @Override
    public Visit createVisit(Visit visit) {
//        visit.setPaymentId(0L);
        visit.setStartDateOfVisit(LocalDate.now());
        visit.setArchived(false);
        visitRepository.create(visit);
        return visit;
    }

    @Override
    public Visit updateVisit(Visit visit) {
        visitRepository.update(visit);
        return visit;
    }

    @Override
    public Visit archive(Visit visit, User user) {
        checkAccessPermissions(user);
        visit.setArchived(true);
        visitRepository.update(visit);
        return visit;
    }

    @Override
    public Visit getVisitById(Long visitId) {
        Visit visit = visitRepository.getById(visitId);
        return visit;
    }

    @Override
    public List<Visit> getAllVisitsByUserId(Long userId) {

        return visitRepository.getAllVisitsByUserId(userId);
    }


    private static void checkAccessPermissions(User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }

}

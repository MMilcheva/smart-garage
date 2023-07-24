package com.example.smart_garage.services;

import com.example.smart_garage.enumeration.Currency;
import com.example.smart_garage.enumeration.PaymentStatus;
import com.example.smart_garage.exceptions.UnauthorizedOperationException;
import com.example.smart_garage.models.*;
import com.example.smart_garage.repositories.contracts.PaymentRepository;
import com.example.smart_garage.repositories.contracts.VisitRepository;
import com.example.smart_garage.services.contracts.*;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.ERROR_MESSAGE;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserService userService;
    private final VisitRepository visitRepository;
    private final VisitService visitService;
    private final OrderService orderService;
    private final DiscountService discountService;
    private final String BASE_URL = "https://api.apilayer.com/exchangerates_data/convert";
    private final String API_KEY = "LVoI1XOZtljOcGE17lKO6Rl7iWCchTel";

    private final OkHttpClient httpClient = new OkHttpClient();

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, UserService userService, VisitRepository visitRepository, VisitService visitService, OrderService orderService, DiscountService discountService) {
        this.paymentRepository = paymentRepository;
        this.userService = userService;
        this.visitRepository = visitRepository;
        this.visitService = visitService;
        this.orderService = orderService;
        this.discountService = discountService;
    }


    @Override
    public List<Payment> getAllPayments(Optional<String> search) {
        return paymentRepository.getAllPayments(search);
    }

    @Override
    public Discount getDiscountByUserValidOn(Long userId, LocalDate validOn) {
        return discountService.getDiscountByUserValidOn(userId, validOn);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.getById(id);
    }

    @Override
    public List<Payment> filterAllPayments(PaymentFilterOptions paymentFilterOptions) {
        return paymentRepository.filterAllPayments(paymentFilterOptions);
    }

    @Override
    public void updatePayment(Payment payment) {
        paymentRepository.updatePayment(payment);
    }

    @Override
    public Payment bookPayment(Long paymentId) {
        Payment toBeBooked = paymentRepository.getById(paymentId);
        toBeBooked.setPaymentStatus(PaymentStatus.PAID);
        toBeBooked.setArchived(true);
        toBeBooked.setDateOfPayment(LocalDate.now());
        Visit visit = toBeBooked.getVisit();
        visit.setEndDateOfVisit(LocalDate.now());
        visit.setArchived(true);
        visit.setPayment(toBeBooked);
        paymentRepository.updatePayment(toBeBooked);
        visitService.updateVisit(visit);
        return toBeBooked;
    }

    @Override
    public Payment createPayment(Long visitId, Currency selectedCurrency, Double exchangeRate) {
        Visit visit = visitService.getVisitById(visitId);
        Long userId = visit.getVehicle().getUser().getUserId();

        Double listPrice = orderService.calculateSumOfAllOrdersByVisitId(visitId);
        Double discount = discountService.getDiscountByUserValidOn(userId, LocalDate.now()).getDiscountAmount();
        Double vat = (listPrice - discount) * 20 / 100.0;
        Double totalPriceBGN = listPrice - discount + vat;
        Payment payment = new Payment();
        payment.setListPrice(listPrice);
        payment.setDiscount(discount);
        payment.setVat(vat);
        payment.setTotalPriceBGN(totalPriceBGN);
        payment.setVisit(visit);
        payment.setSelectedCurrency(selectedCurrency);
        payment.setExchangeRate(exchangeRate);
        payment.setPaymentStatus(PaymentStatus.UNPAID);

        Payment savedPayment = paymentRepository.createPayment(payment);
        visit.setPayment(savedPayment);
        visitService.updateVisit(visit);
        return payment;
    }


    @Override
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.getById(paymentId);
        Visit visit = visitService.getVisitById(payment.getVisit().getVisitId());
        paymentRepository.delete(paymentId);
        visitService.updateVisit(visit);
    }

    @Override
    public List<Payment> getAllPaymentsByUserId(Long userId) {
        return paymentRepository.getAllPaymentsByUserId(userId);
    }

    @Override
    public List<Payment> getAllDuePaymentsByUserId(Long userId) {
        return paymentRepository.getAllDuePaymentsByUserId(userId);
    }

    @Override
    public List<Payment> getAllPaidPaymentsByUserId(Long userId) {
        return paymentRepository.getAllPaidPaymentsByUserId(userId);
    }


    @Override
    public void deletePayment(long paymentId, User user) {
        checkAccessPermissions(user);
        Payment payment = paymentRepository.getById(paymentId);
        Visit visit = visitService.getVisitById(payment.getVisit().getVisitId());
        visit.setPayment(null);
        visitService.updateVisit(visit);
        paymentRepository.delete(paymentId);
    }
    private static void checkAccessPermissions(User executingUser) {
        if (!executingUser.getRole().getRoleName().equals("admin")) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}



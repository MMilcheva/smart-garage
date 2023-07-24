package com.example.smart_garage.services.contracts;

import com.example.smart_garage.enumeration.Currency;
import com.example.smart_garage.models.Discount;
import com.example.smart_garage.models.Payment;
import com.example.smart_garage.models.PaymentFilterOptions;
import com.example.smart_garage.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentService {


    List<Payment> getAllPayments(Optional<String> search);

    Discount getDiscountByUserValidOn(Long userId, LocalDate validOn);

    Payment getPaymentById (Long id);

    List<Payment> filterAllPayments(PaymentFilterOptions paymentFilterOptions);

    Payment bookPayment(Long paymentId);

    void updatePayment(Payment payment);

    Payment createPayment(Long visitId, Currency selectedCurrency, Double exchangeRate);


    void deletePayment(Long paymentId);

    List<Payment> getAllPaymentsByUserId(Long userId);

    List<Payment> getAllDuePaymentsByUserId(Long userId);

    List<Payment> getAllPaidPaymentsByUserId(Long userId);

    void deletePayment(long paymentId, User user);
}

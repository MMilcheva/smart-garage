package com.example.smart_garage.repositories.contracts;

import com.example.smart_garage.models.*;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends BaseCRUDRepository<Payment>{

    List<Payment> getAllPayments(Optional<String> search);

    List<Payment> filterAllPayments(PaymentFilterOptions paymentFilterOptions);

    List<Payment> getAllPaymentsByUserId(Long userId);

    List<Payment> getAllDuePaymentsByUserId(Long userId);

    List<Payment> getAllPaidPaymentsByUserId(Long userId);

    void updatePayment(Payment payment);

    void updatePaymentStatus(Long paymentId);

    Payment createPayment(Payment payment);

    void deletePayment(Long paymentId);



}

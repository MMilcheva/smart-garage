package com.example.smart_garage.helpers;

import com.example.smart_garage.dto.PaymentResponse;
import com.example.smart_garage.dto.PaymentSaveRequest;
import com.example.smart_garage.models.Payment;
import com.example.smart_garage.models.Visit;
import com.example.smart_garage.services.contracts.CurrencyConversionService;
import com.example.smart_garage.services.contracts.DiscountService;
import com.example.smart_garage.services.contracts.PaymentService;
import com.example.smart_garage.services.contracts.VisitService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentMapper {

    private final PaymentService paymentService;

    private final VisitService visitService;
    private final DiscountService discountService;
    private final CurrencyConversionService currencyConversionService;

    public PaymentMapper(PaymentService paymentService, VisitService visitService, DiscountService discountService, CurrencyConversionService currencyConversionService) {
        this.paymentService = paymentService;
        this.visitService = visitService;
        this.discountService = discountService;
        this.currencyConversionService = currencyConversionService;
    }

    public PaymentResponse convertToPaymentResponse(Payment payment) {

        PaymentResponse paymentResponse = new PaymentResponse();
        if (payment != null) {

            paymentResponse.setPaymentId(payment.getPaymentId());

            double exchangeRate = payment.getExchangeRate();
            paymentResponse.setExchangeRate(payment.getExchangeRate());

            paymentResponse.setVisit(payment.getVisit());
            paymentResponse.setPaymentStatus(payment.getPaymentStatus());


            Double listPrice = payment.getListPrice();
            Double listPriceSelectedCurrency = Math.round(payment.getListPrice() * exchangeRate * 100.0) / 100.0;
            paymentResponse.setListPrice(listPrice);
            paymentResponse.setListPriceSelectedCurrency(listPriceSelectedCurrency);

            double discount = payment.getDiscount();
            double discountSelectedCurrency = Math.round(payment.getDiscount() * exchangeRate * 100.0) / 100.0;
            paymentResponse.setDiscount(discount);
            paymentResponse.setDiscountSelectedCurrency(discountSelectedCurrency);

            double vat = Math.round(payment.getVat() * exchangeRate * 100.0) / 100.0;
            double vatSelectedCurrency = payment.getVat();
            paymentResponse.setVat(vat);
            paymentResponse.setVatSelectedCurrency(vatSelectedCurrency);

            double total = payment.getTotalPriceBGN();
            double totalPriceSelectedCurrency = Math.round(payment.getTotalPriceBGN() * exchangeRate * 100.0) / 100.0;
            paymentResponse.setTotalPriceBGN(total);
            paymentResponse.setTotalPriceSelectedCurrency(totalPriceSelectedCurrency);

            paymentResponse.setSelectedCurrency(payment.getSelectedCurrency());
            paymentResponse.setDateOfPayment(payment.getDateOfPayment());
        }
        return paymentResponse;
    }

    public Payment convertToPayment(PaymentSaveRequest paymentSaveRequest) throws IOException {
        Payment payment = new Payment();
        Visit visit = visitService.getVisitById(paymentSaveRequest.getVisitId());
        LocalDate dateOfVisit = visit.getStartDateOfVisit();
        if (dateOfVisit.equals(LocalDate.now())){
            dateOfVisit=LocalDate.now().minusDays(1);
        }
        Double exchangeRate = currencyConversionService.getExchangerateOnDate(dateOfVisit, paymentSaveRequest.getSelectedCurrency().getValue, "BGN");
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);
        System.out.println("Formatted date: " + formattedDate);

        if (exchangeRate == null) {
            switch (paymentSaveRequest.getSelectedCurrency().getValue) {
                case "EUR" -> exchangeRate = 0.5555555;
                case "USD" -> exchangeRate = 0.5555555;
                case "CHF" -> exchangeRate = 0.5555555;
                case "GBR" -> exchangeRate = 0.5555555;
            }
        }

        payment.setExchangeRate(exchangeRate);
        payment.setVisit(visit);
        payment.setSelectedCurrency(paymentSaveRequest.getSelectedCurrency());
        return payment;
    }

    public List<PaymentResponse> convertToPaymentResponses(List<Payment> payments) {

        List<PaymentResponse> paymentResponses = new ArrayList<>();
        payments.forEach(payment -> paymentResponses.add(convertToPaymentResponse(payment)));
        return paymentResponses;
    }
}

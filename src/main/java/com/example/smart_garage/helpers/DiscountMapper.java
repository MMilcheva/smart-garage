package com.example.smart_garage.helpers;

import com.example.smart_garage.dto.DiscountResponse;
import com.example.smart_garage.dto.DiscountSaveRequest;
import com.example.smart_garage.models.Discount;
import com.example.smart_garage.models.User;
import com.example.smart_garage.services.contracts.DiscountService;
import com.example.smart_garage.services.contracts.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class DiscountMapper {


    private final DiscountService discountService;
    private final UserService userService;

    public DiscountMapper(DiscountService discountService, UserService userService) {
        this.discountService = discountService;
        this.userService = userService;
    }


    public Discount convertToDiscount(DiscountSaveRequest discountSaveRequest) {
        Discount discount = new Discount();
        discount.setDiscountName(discountSaveRequest.getDiscountName());
        discount.setDiscountAmount(discountSaveRequest.getDiscountAmount());
        User user = userService.getUserById(discountSaveRequest.getUserId());
        discount.setUser(user);

        LocalDate date = discountSaveRequest.getValidFrom();
        if (date == null) {
            date = LocalDate.now();
        }

        discount.setValidFrom(date);
        discount.setValidTo(discountSaveRequest.getValidTo());
        return discount;
    }

    public DiscountResponse convertToDiscountResponse(Discount discount) {

        DiscountResponse discountResponse = new DiscountResponse();
        discountResponse.setDiscountName(discount.getDiscountName());
        discountResponse.setDiscountAmount(discount.getDiscountAmount());
        discountResponse.setUser(discount.getUser());
        discountResponse.setValidFrom(discount.getValidFrom());
        discountResponse.setValidTo(discount.getValidTo());

        return discountResponse;
    }

    public List<DiscountResponse> convertToDiscountResponses(List<Discount> discounts) {

        List<DiscountResponse> discountResponses = new ArrayList<>();

        discounts.forEach(model -> discountResponses.add(convertToDiscountResponse(model)));
        return discountResponses;
    }


}

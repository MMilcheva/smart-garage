package com.example.smart_garage.services;

import com.example.smart_garage.exceptions.AuthorizationException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.models.Discount;
import com.example.smart_garage.models.DiscountFilterOptions;
import com.example.smart_garage.models.User;
import com.example.smart_garage.repositories.contracts.DiscountRepository;
import com.example.smart_garage.services.contracts.DiscountService;
import com.example.smart_garage.services.contracts.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.MODIFY_MODEL_ERROR_MESSAGE;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final UserService userService;

    public DiscountServiceImpl(DiscountRepository discountRepository, UserService userService) {
        this.discountRepository = discountRepository;
        this.userService = userService;
    }

    @Override
    public List<Discount> getAllDiscounts(Optional<String> search) {

        return discountRepository.getAllDiscounts(search);
    }

    @Override
    public Discount getDiscountById(Long discountId) {
        return discountRepository.getById(discountId);
    }

    @Override
    public Discount getDiscountByUserValidOn(Long userId, LocalDate validOn) {
        return discountRepository.getDiscountByUserValidOn(userId, validOn);
    }

    @Override
    public List<Discount> getAllDiscountsByUserId(Long userId) {
        return discountRepository.getAllDiscountsByUserId(userId);
    }

    @Override
    public List<Discount> getAllDiscountsFilter(DiscountFilterOptions discountFilterOptions) {
        return discountRepository.getAllDiscountsFilter(discountFilterOptions);
    }

    @Override
    public void deleteDiscount(long discountId, User user) {
        checkModifyPermissions(user);
        discountRepository.delete(discountId);
    }

    @Override
    public Discount createDiscount(Discount discount, User user) {

        Long userId = discount.getUser().getUserId();

        List<Discount> discounts = discountRepository.getAllDiscountsByUserId(userId);
        LocalDate validFromDate = discount.getValidFrom();
        LocalDate endDate = LocalDate.of(2099, 12, 31);

        if (discounts.size() > 0) {
            Discount oldDiscount = discountRepository.getDiscountByUserValidOn(userId, validFromDate);

            if (oldDiscount.getValidFrom().isEqual(validFromDate)) {
                throw new DuplicateEntityException("Discount", "discount", oldDiscount.getValidFrom().toString());
            } else {
                LocalDate previousDate = validFromDate.minusDays(1);
                oldDiscount.setValidTo(previousDate);
                updateDiscount(oldDiscount, user);
            }
        }
        discount.setValidFrom(validFromDate);
        discount.setValidTo(endDate);
        discountRepository.create(discount);
        return discount;
    }


    @Override
    public Discount updateDiscount(Discount discount, User user) {
        checkModifyPermissions(user);

        discountRepository.update(discount);
        return discount;
    }

    @Override
    public Discount archiveDiscount(Discount discount) {
        discount.setValidTo(LocalDate.now());
        discount.setArchived(true);
        discountRepository.update(discount);
        return discount;
    }

    private void checkModifyPermissions(User user) {
        String str = "admin";
        //TODO to check why throw exc while equals is applied
        if (!(user.getRole().getRoleName().equals(str))) {
            throw new AuthorizationException(MODIFY_MODEL_ERROR_MESSAGE);
        }
    }
}

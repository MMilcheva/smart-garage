package com.example.smart_garage.services;

import com.example.smart_garage.exceptions.AuthorizationException;
import com.example.smart_garage.exceptions.DuplicateEntityException;
import com.example.smart_garage.models.Price;
import com.example.smart_garage.models.PriceFilterOptions;
import com.example.smart_garage.models.User;
import com.example.smart_garage.repositories.contracts.PriceRepository;
import com.example.smart_garage.services.contracts.PriceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.smart_garage.constants.ExceptionConstant.MODIFY_MODEL_ERROR_MESSAGE;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public List<Price> getAllPrices(Optional<String> search) {
        return priceRepository.getAllPrices(search);
    }


    @Override
    public List<Price> getAllPricesFilter(PriceFilterOptions priceFilterOptions) {
        return priceRepository.getAllPricesFilter(priceFilterOptions);
    }

    @Override
    public Price getPriceById(Long priceId) {
        LocalDate validOn = LocalDate.now();
        return getPriceByIdValidOn(priceId, validOn);
    }

    @Override
    public Price getPriceByIdValidOn(Long priceId, LocalDate validOn) {
        Price price = priceRepository.getById(priceId);
        price = priceRepository.getPriceValidOn(price.getCarMaintenance().getCarMaintenanceId(), validOn);
        return price;
    }

    @Override
    public void updatePrice(Price price, User user) {
        checkModifyPermissions(user);
        priceRepository.update(price);
    }

    @Override
    public Price createPrice(Price price, User user) {

        Long carMaintenanceId = price.getCarMaintenance().getCarMaintenanceId();
        List<Price> prices = priceRepository.getPriceByCarMaintenanceId(carMaintenanceId);
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = LocalDate.of(2099, 12, 31);

        if (prices.size() > 0) {
            Price oldPrice = priceRepository.getPriceValidOn(carMaintenanceId, LocalDate.now());

            if (oldPrice.getValidFrom().isEqual(currentDate)) {
                throw new DuplicateEntityException("Price", "price", oldPrice.getValidFrom().toString());
            } else {
                LocalDate previousDate = LocalDate.now().minusDays(1);
                oldPrice.setValidTo(previousDate);
                updatePrice(oldPrice, user);
            }
        }
        price.setValidFrom(currentDate);
        price.setValidTo(endDate);
        priceRepository.create(price);

        return price;
    }
    @Override
    public void deletePrice(Long priceId, User user) {
        checkModifyPermissions(user);
        priceRepository.delete(priceId);
    }

    private void checkModifyPermissions(User user) {
        String str = "admin";
        if (!(user.getRole().getRoleName().equals(str))) {
            throw new AuthorizationException(MODIFY_MODEL_ERROR_MESSAGE);
        }
    }
}

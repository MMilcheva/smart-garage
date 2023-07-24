package com.example.smart_garage.helpers;

import com.example.smart_garage.dto.*;
import com.example.smart_garage.models.*;
import com.example.smart_garage.services.contracts.CarMaintenanceService;
import com.example.smart_garage.services.contracts.PriceService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PriceMapper {

    private final PriceService priceService;
private final CarMaintenanceService carMaintenanceService;
    public PriceMapper(PriceService priceService, CarMaintenanceService carMaintenanceService) {
        this.priceService = priceService;
        this.carMaintenanceService = carMaintenanceService;
    }


    public PriceResponse convertToPriceResponse(Price price) {
        PriceResponse priceResponse = new PriceResponse();

        priceResponse.setCarMaintenanceName(price.getCarMaintenance().getCarMaintenanceName());
        priceResponse.setAmount(price.getAmount());
        priceResponse.setValidFrom(price.getValidFrom());
        priceResponse.setValidTo(price.getValidTo());
        return priceResponse;
    }

    public Price convertToPrice(PriceSaveRequest priceSaveRequest) {
        Price price = new Price();

        CarMaintenance carMaintenance = carMaintenanceService.getCarMaintenanceById(priceSaveRequest.getCarMaintenanceId());
       price.setCarMaintenance(carMaintenance);
        price.setAmount(priceSaveRequest.getAmount());
        price.setValidFrom(priceSaveRequest.getValidFrom());
        price.setValidTo(priceSaveRequest.getValidTo());
        return price;
    }
    public List<PriceResponse> convertToPriceResponses(List<Price> prices) {

        List<PriceResponse> priceResponses = new ArrayList<>();

        prices.forEach(price->priceResponses.add(convertToPriceResponse(price)));
        return priceResponses;
    }

    public List<PriceResponse> convertToPriceResponse(List<Price> prices) {

        List<PriceResponse> priceResponses = new ArrayList<>();

        prices.forEach(price -> {
            PriceResponse priceResponse = new PriceResponse();
            priceResponse.setAmount(price.getAmount());
            priceResponse.setValidFrom(price.getValidFrom());
            priceResponse.setValidTo(price.getValidTo());
            priceResponses.add(priceResponse);
        });

        return priceResponses;
    }
}

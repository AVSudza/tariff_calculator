package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.fastdelivery.domain.common.distanceBaseCost.DistanceBaseCostProvider;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final WeightPriceProvider weightPriceProvider;
    private final DistanceBaseCostProvider distanceBaseCostProvider;

    public Price calc(Shipment shipment) {
        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        var volumeAllPackagesMeter = shipment.volumeAllPackages().cubicMeters();
        var minimalPrice = weightPriceProvider.minimalPrice();

        Price weightPrice = weightPriceProvider
                .costPerKg()
                .multiply(weightAllPackagesKg)
                .max(minimalPrice);
        log.info("**************************** Calculation delivery! ****************************");
        log.info("Price for weight: " + weightPrice.amount().setScale(2, RoundingMode.HALF_UP));
        Price volumePrice = weightPriceProvider
                .costPerCubicMeter()
                .multiply(volumeAllPackagesMeter)
                .max(minimalPrice);
        Price baseCost = weightPrice.max(volumePrice);
        log.info("Price for volume: " + volumePrice.amount().setScale(2, RoundingMode.HALF_UP));
        log.info("Base cost: " + baseCost.amount().setScale(2, RoundingMode.HALF_UP));
        return calcShippingCost(baseCost, shipment);
    }

    private Price calcShippingCost(Price baseCost, Shipment shipment) {
        BigDecimal distanceBaseCost = BigDecimal.valueOf(distanceBaseCostProvider.getDistanceBaseCost())
                .setScale(2, RoundingMode.HALF_UP);
        log.info("Distance base cost: " + distanceBaseCost);
        BigDecimal distanceShipment = BigDecimal.valueOf(getDistanceShipment(shipment).longValue())
                .setScale(2, RoundingMode.HALF_UP);
        log.info("Distance shipment: " + distanceShipment);
        Price shippingCost;
        if (distanceShipment.compareTo(distanceBaseCost) > 0) {
            shippingCost = new Price(distanceShipment
                    .divide(distanceBaseCost, 100, RoundingMode.UP)
                    .multiply(baseCost.amount())
                    .setScale(2, RoundingMode.UP),
                    baseCost.currency());
        } else {
            shippingCost = baseCost;
        }
        log.info("Shipping cost: " + shippingCost);
        return shippingCost;
    }

    private BigDecimal getDistanceShipment(Shipment shipment) {
        return new HaversinesWithAntipodes(
                shipment.departure().getLatitude(), shipment.departure().getLongitude(),
                shipment.destination().getLatitude(), shipment.destination().getLongitude())
                .getDistance();
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }
}

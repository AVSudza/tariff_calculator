package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final WeightPriceProvider weightPriceProvider;
    private final CoordinatesPropertiesProvider coordinatesPropertiesProvider;

    public Price calc(Shipment shipment) {
        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        var volumeAllPackagesMeter = shipment.volumeAllPackages().cubicMeters();
        var minimalPrice = weightPriceProvider.minimalPrice();
        coordinatesPropertiesProvider.isPossible(shipment);

        Price weightPrice = weightPriceProvider
                .costPerKg()
                .multiply(weightAllPackagesKg)
                .max(minimalPrice);

        Price volumePrice = weightPriceProvider
                .costPerCubicMeter()
                .multiply(volumeAllPackagesMeter)
                .max(minimalPrice);
        return weightPrice.max(volumePrice);
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }
}

package ru.fastdelivery.domain.delivery.points;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class DestinationPointFactory {

    private final CoordinatesPropertiesProvider provider;

    public DestinationPoint create(BigDecimal latitude, BigDecimal longitude) throws IllegalArgumentException {
        DestinationPoint destinationPoint = new DestinationPoint(latitude, longitude);
        destinationPoint.isPossible(provider, "");
        return new DestinationPoint(latitude, longitude);
    }
}

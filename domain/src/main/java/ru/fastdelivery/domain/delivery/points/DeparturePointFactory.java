package ru.fastdelivery.domain.delivery.points;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
@RequiredArgsConstructor
public class DeparturePointFactory {
    private final CoordinatesPropertiesProvider provider;

    public DeparturePoint create(BigDecimal latitude, BigDecimal longitude) throws IllegalArgumentException {
        DeparturePoint departurePoint = new DeparturePoint(latitude, longitude);
        departurePoint.isPossible(provider, "");
        return new DeparturePoint(latitude, longitude);
    }

}

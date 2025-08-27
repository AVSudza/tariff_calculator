package ru.fastdelivery.domain.delivery.points;

import java.math.BigDecimal;

/**
 * Координаты пункта отправления
 */
public class DeparturePoint extends CoordinatePoint {
    public DeparturePoint() {
        super(BigDecimal.ZERO, BigDecimal.ZERO);
    }
    public DeparturePoint(BigDecimal latitude, BigDecimal longitude) {
        super(latitude, longitude);
    }

    @Override
    public boolean isPossible(CoordinatesPropertiesProvider coordinatesPropertiesProvider, String point) throws IllegalArgumentException {
        return super.isPossible(coordinatesPropertiesProvider, "departure");
    }
}

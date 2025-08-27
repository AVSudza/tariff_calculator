package ru.fastdelivery.domain.delivery.points;

import java.math.BigDecimal;

/**
 * Координаты пункта назначения
 */
public class DestinationPoint extends CoordinatePoint {
    public DestinationPoint() {
        super(BigDecimal.ZERO, BigDecimal.ZERO);
    }
    public DestinationPoint(BigDecimal latitude, BigDecimal longitude) {
        super(latitude, longitude);
    }

    @Override
    public boolean isPossible(CoordinatesPropertiesProvider coordinatesPropertiesProvider, String point) throws IllegalArgumentException {
        return super.isPossible(coordinatesPropertiesProvider, "destination");
    }
}

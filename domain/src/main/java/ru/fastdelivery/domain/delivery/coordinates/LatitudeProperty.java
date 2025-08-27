package ru.fastdelivery.domain.delivery.coordinates;

import java.math.BigDecimal;

public class LatitudeProperty extends CoordinateRangeProperty {
    public LatitudeProperty(BigDecimal min, BigDecimal max) {
        super(min, max);
    }
}

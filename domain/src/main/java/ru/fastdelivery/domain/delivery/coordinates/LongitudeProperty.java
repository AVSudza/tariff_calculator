package ru.fastdelivery.domain.delivery.coordinates;

import java.math.BigDecimal;

public class LongitudeProperty extends CoordinateRangeProperty {
    public LongitudeProperty(BigDecimal min, BigDecimal max) {
        super(min, max);
    }
}

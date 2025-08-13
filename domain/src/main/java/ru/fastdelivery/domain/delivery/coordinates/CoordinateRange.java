package ru.fastdelivery.domain.delivery.coordinates;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class CoordinateRange {
    private BigDecimal min;
    private BigDecimal max;
}

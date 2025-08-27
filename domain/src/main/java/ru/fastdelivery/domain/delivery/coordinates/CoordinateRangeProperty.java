package ru.fastdelivery.domain.delivery.coordinates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CoordinateRangeProperty {
    private BigDecimal min;
    private BigDecimal max;
}

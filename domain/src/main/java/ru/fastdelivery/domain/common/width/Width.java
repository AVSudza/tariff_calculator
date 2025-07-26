package ru.fastdelivery.domain.common.width;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Общий класс ширины
 *
 * @param widthMills ширина в миллиметрах
 */

public record Width(BigInteger widthMills) implements Comparable<Width> {

    public Width {
        if (widthMills == null) {
            throw new IllegalArgumentException("Width cannot be empty!");
        }
        if (isLessThanZero(widthMills)) {
            throw new IllegalArgumentException("Width cannot be below Zero!");
        }
    }

    private static boolean isLessThanZero(BigInteger width) {
        return BigInteger.ZERO.compareTo(width) > 0;
    }

    public static Width zero() {
        return new Width(BigInteger.ZERO);
    }

    public BigDecimal meters() {
        return new BigDecimal(widthMills)
                .divide(BigDecimal.valueOf(1000), 100, RoundingMode.HALF_UP);
    }

    public Width add(Width additionalWidth) {
        return new Width(this.widthMills.add(additionalWidth.widthMills));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Width width = (Width) o;
        return widthMills.compareTo(width.widthMills) == 0;
    }

    @Override
    public int compareTo(Width w) {
        return w.widthMills.compareTo(widthMills);
    }

    public boolean greaterThan(Width w) {
        return widthMills.compareTo(w.widthMills) > 0;
    }}

package ru.fastdelivery.domain.common.height;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Общий класс высоты
 *
 * @param heightMills высота в миллиметрах
 */

public record Height(BigInteger heightMills) implements Comparable<Height> {

    public Height {
        if (heightMills == null) {
            throw new IllegalArgumentException("Height cannot be empty!");
        }
        if (isLessThanZero(heightMills)) {
            throw new IllegalArgumentException("Height cannot be below Zero!");
        }
    }

    private static boolean isLessThanZero(BigInteger height) {
        return BigInteger.ZERO.compareTo(height) > 0;
    }

    public static Height zero() {
        return new Height(BigInteger.ZERO);
    }

    public BigDecimal meters() {
        return new BigDecimal(heightMills)
                .divide(BigDecimal.valueOf(1000), 100, RoundingMode.HALF_UP);
    }

    public Height add(Height additionalWeight) {
        return new Height(this.heightMills.add(additionalWeight.heightMills));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Height height = (Height) o;
        return heightMills.compareTo(height.heightMills) == 0;
    }


    @Override
    public int compareTo(Height h) {
        return h.heightMills.compareTo(heightMills);
    }

    public boolean greaterThan(Height h) {
        return heightMills.compareTo(h.heightMills) > 0;
    }
}

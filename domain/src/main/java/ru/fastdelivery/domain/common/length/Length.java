package ru.fastdelivery.domain.common.length;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Общий класс длины
 *
 * @param lengthMills длина в миллиметрах
 */

public record Length(BigInteger lengthMills) implements Comparable<Length> {

    public Length {
        if (lengthMills == null) {
            throw new IllegalArgumentException("Length cannot be empty!");
        }
        if (isLessThanZero(lengthMills)) {
            throw new IllegalArgumentException("Length cannot be below Zero!");
        }
    }

    private static boolean isLessThanZero(BigInteger length) {
        return BigInteger.ZERO.compareTo(length) > 0;
    }

    public static Length zero() {
        return new Length(BigInteger.ZERO);
    }

    public BigDecimal meters() {
        return new BigDecimal(lengthMills)
                .divide(BigDecimal.valueOf(1000), 100, RoundingMode.HALF_UP);
    }

    public Length add(Length additionalWeight) {
        return new Length(this.lengthMills.add(additionalWeight.lengthMills));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Length length = (Length) o;
        return lengthMills.compareTo(length.lengthMills) == 0;
    }


    @Override
    public int compareTo(Length w) {
        return w.lengthMills().compareTo(lengthMills);
    }

    public boolean greaterThan(Length w) {
        return lengthMills.compareTo(w.lengthMills) > 0;
    }
}
package ru.fastdelivery.domain.common.volume;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Общий класс объема
 *
 * @param volumeCubicMills вес в кубических миллиметрах
 */
public record Volume(BigInteger volumeCubicMills) implements Comparable<Volume> {

    public Volume {
        if (volumeCubicMills == null) {
            throw new IllegalArgumentException("Volume cannot be empty!");
        }
        if (isLessThanZero(volumeCubicMills)) {
            throw new IllegalArgumentException("Volume cannot be below Zero!");
        }
    }

    private static boolean isLessThanZero(BigInteger weight) {
        return BigInteger.ZERO.compareTo(weight) > 0;
    }

    public static Volume zero() {
        return new Volume(BigInteger.ZERO);
    }

    public BigDecimal cubicMeters() {
        return new BigDecimal(volumeCubicMills)
                .divide(BigDecimal.valueOf(1_000_000_000), 4, RoundingMode.HALF_UP)
                .setScale(4, RoundingMode.CEILING);
    }

    public Volume add(Volume additionalVolume) {
        return new Volume(this.volumeCubicMills.add(additionalVolume.volumeCubicMills));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Volume volume = (Volume) o;
        return volumeCubicMills.compareTo(volume.volumeCubicMills) == 0;
    }

    @Override
    public int compareTo(Volume v) {
        return v.volumeCubicMills.compareTo(volumeCubicMills);
    }

    public boolean greaterThan(Volume v) {
        return volumeCubicMills.compareTo(v.volumeCubicMills) > 0;
    }
}

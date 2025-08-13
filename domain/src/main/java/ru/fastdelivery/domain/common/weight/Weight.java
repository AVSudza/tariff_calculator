package ru.fastdelivery.domain.common.weight;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Общий класс веса
 *
 * @param weightGrams вес в граммах
 */
public record Weight(BigInteger weightGrams, int maxWeight) implements Comparable<Weight> {

    public Weight(BigInteger weightGrams, int maxWeight) {
        this.weightGrams = weightGrams;
        this.maxWeight = maxWeight;
        if (weightGrams == null) {
            throw new IllegalArgumentException("Weight cannot be empty!");
        }
        if (isLessThanZero(weightGrams)) {
            throw new IllegalArgumentException("Weight cannot be below Zero!");
        }
        if (greaterThan(BigInteger.valueOf(maxWeight))) {
            throw new IllegalArgumentException("Weight cannot be higher than " + maxWeight + "!");
        }
    }

    private static boolean isLessThanZero(BigInteger weight) {
        return BigInteger.ZERO.compareTo(weight) > 0;
    }

    public static Weight zero() {
        return new Weight(BigInteger.ZERO, Integer.MAX_VALUE);
    }

    public BigDecimal kilograms() {
        return new BigDecimal(weightGrams)
                .divide(BigDecimal.valueOf(1000), 100, RoundingMode.HALF_UP);
    }

    public Weight add(Weight additionalWeight) {
        return new Weight(this.weightGrams.add(additionalWeight.weightGrams), maxWeight);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Weight weight = (Weight) o;
        return weightGrams.compareTo(weight.weightGrams) == 0;
    }


    @Override
    public int compareTo(Weight w) {
        return w.weightGrams().compareTo(weightGrams());
    }

    public boolean greaterThan(Weight w) {
        return weightGrams().compareTo(w.weightGrams()) > 0;
    }

    public boolean greaterThan(BigInteger maxWeight) {
        return weightGrams().compareTo(maxWeight) > 0;
    }
}

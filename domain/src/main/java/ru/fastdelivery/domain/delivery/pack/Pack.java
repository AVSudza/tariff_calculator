package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.common.height.Height;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.common.width.Width;

import java.math.BigInteger;

/**
 * Упаковка груза
 *
 * @param weight вес товаров в упаковке
 */
public record Pack(Weight weight, Length length, Width width, Height height) {

    private static final Weight maxWeight = new Weight(BigInteger.valueOf(150_000));
    private static final Length maxLength = new Length(BigInteger.valueOf(1_500_000));
    private static final Width maxWidth = new Width(BigInteger.valueOf(1_500_000));
    private static final Height maxHeight = new Height(BigInteger.valueOf(1_500_000));
    private static final BigInteger stepNormalize = new BigInteger(String.valueOf(50));

    public Pack {
        if (weight.greaterThan(maxWeight)) {
            throw new IllegalArgumentException("Package weight can't be more than " + maxWeight);
        }
        if (length.greaterThan(maxLength)) {
            throw new IllegalArgumentException("Package length can't be more than " + maxLength);
        }
        if (width.greaterThan(maxWidth)) {
            throw new IllegalArgumentException("Package width can't be more than " + maxWidth);
        }
        if (height.greaterThan(maxHeight)) {
            throw new IllegalArgumentException("Package height can't be more than " + maxHeight);
        }
    }

    public Volume getVolume() {
        BigInteger normalWidth = normalize(width.widthMills());
        BigInteger normalLength = normalize(length.lengthMills());
        BigInteger normalHeight = normalize(height.heightMills());
        return new Volume(normalWidth.multiply(normalLength).multiply(normalHeight));
    }

    private BigInteger normalize(BigInteger dimension) {
        BigInteger remainderValue = dimension.remainder(stepNormalize);
        return dimension.add((remainderValue.intValue() == 0) ? BigInteger.ZERO : stepNormalize.subtract(dimension.remainder(stepNormalize)));
    }
}

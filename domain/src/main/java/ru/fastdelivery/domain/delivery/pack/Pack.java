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
 * @param length длина упаковки
 * @param width ширина упаковки
 * @param height высота упаковки
 * @param stepNormalize шаг нормализации размеров
 */
public record Pack(Weight weight, Length length, Width width, Height height, BigInteger stepNormalize) {

    public Pack {
        if (weight.greaterThan(new Weight(BigInteger.valueOf(weight.maxWeight()), weight.maxWeight()))) {
            throw new IllegalArgumentException("Package can't be more than " + weight.maxWeight());
        }
    }
    public Volume getVolume() {
        return new Volume(width.widthMills().multiply(length.lengthMills()).multiply(height.heightMills())) ;
    }

    public Volume getNormalizeVolume() {
        BigInteger normalWidth = normalize(width.widthMills());
        BigInteger normalLength = normalize(length.lengthMills());
        BigInteger normalHeight = normalize(height.heightMills());
        return new Volume(normalWidth.multiply(normalLength).multiply(normalHeight));
    }

    private BigInteger normalize(BigInteger dimension) {
        BigInteger remainderValue = dimension.remainder(stepNormalize);
        return dimension.add((remainderValue.intValue() == 0) ? BigInteger.ZERO
                : stepNormalize.subtract(dimension.remainder(stepNormalize)));
    }
}

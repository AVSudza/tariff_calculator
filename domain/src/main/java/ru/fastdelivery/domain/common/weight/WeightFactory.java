package ru.fastdelivery.domain.common.weight;

import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

/**
 * Создание веса упаковки с проверками
 */
@RequiredArgsConstructor
public class WeightFactory {

    private final WeightPropertiesProvider weightPropertiesProvider;

    public Weight create(BigInteger weightGrams) {
        return new Weight(weightGrams, weightPropertiesProvider.getMax());
    }
}

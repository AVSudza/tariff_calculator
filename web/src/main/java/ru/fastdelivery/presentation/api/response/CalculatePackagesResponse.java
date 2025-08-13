package ru.fastdelivery.presentation.api.response;

import ru.fastdelivery.domain.common.price.Price;

import java.math.BigDecimal;

/**
 * Ответ на вычисление цены доставки пакетов
 *
 * @param totalPrice Общая цена доставки пакетов
 * @param minimalPrice Минимальная цена доставки пакетов
 * @param currencyCode Трехбуквенный код валюты цены доставки пакетов
 */
public record CalculatePackagesResponse(
        BigDecimal totalPrice,
        BigDecimal minimalPrice,
        String currencyCode) {

    public CalculatePackagesResponse(Price totalPrice, Price minimalPrice) {
        this(totalPrice.amount(), minimalPrice.amount(), totalPrice.currency().getCode());

        if (currencyIsNotEqual(totalPrice, minimalPrice)) {
            throw new IllegalArgumentException("Currency codes must be the same");
        }
    }

    private static boolean currencyIsNotEqual(Price priceLeft, Price priceRight) {
        return !priceLeft.currency().equals(priceRight.currency());
    }
}

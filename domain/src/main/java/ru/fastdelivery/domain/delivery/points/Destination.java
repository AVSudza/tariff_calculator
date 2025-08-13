package ru.fastdelivery.domain.delivery.points;

import java.math.BigDecimal;

/**
 * Координаты пункта назначения
 *
 * @param latitude Широта пункта назначения
 * @param longitude Долгота пункта назначения
 */
public record Destination(
        BigDecimal latitude,
        BigDecimal longitude) {
}

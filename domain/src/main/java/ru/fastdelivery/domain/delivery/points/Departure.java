package ru.fastdelivery.domain.delivery.points;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Координаты пункта отправления
 *
 * @param latitude  Широта пункта отправления
 * @param longitude Долгота пункта отправления
 */
public record Departure(
        @Valid @NotNull BigDecimal latitude,
        @Valid @NotNull BigDecimal longitude) {
}

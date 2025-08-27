package ru.fastdelivery.domain.delivery.points;

import ru.fastdelivery.domain.delivery.coordinates.CoordinatesProperty;

/**
 * Проверка допустимости значений широты и долготы пунктов отправления и назначения
 */

public interface CoordinatesPropertiesProvider {
    boolean isPossible() throws IllegalArgumentException;
    CoordinatesProperty coordinates();
}

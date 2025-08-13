package ru.fastdelivery.usecase;

import ru.fastdelivery.domain.delivery.shipment.Shipment;

/**
 * Проверка допустимости значений широты и долготы пунктов отправления и назначения
 */

public interface CoordinatesPropertiesProvider {
    boolean isPossible(Shipment shipment);

}

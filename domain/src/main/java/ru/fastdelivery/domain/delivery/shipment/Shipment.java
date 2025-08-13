package ru.fastdelivery.domain.delivery.shipment;

import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.points.Departure;
import ru.fastdelivery.domain.delivery.points.Destination;

import java.util.List;

/**
 * Отгрузка
 *
 * @param packages Список упаковок в грузе
 * @param currency Валюта объявленная для груза
 */
public record Shipment(
        List<Pack> packages, Currency currency,
        Destination destination, Departure departure) {
    public Weight weightAllPackages() {
        return packages.stream()
                .map(Pack::weight)
                .reduce(Weight.zero(), Weight::add);
    }
    public Volume volumeAllPackages() {
        return packages.stream()
                .map(Pack::getNormalizeVolume)
                .reduce(Volume.zero(), Volume::add);
    }
}

package ru.fastdelivery.domain.delivery.shipment;

import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.volume.Volume;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.points.DeparturePoint;
import ru.fastdelivery.domain.delivery.points.DestinationPoint;

import java.util.List;

/**
 * Отгрузка
 *
 * @param packages Список упаковок в грузе
 * @param currency Валюта объявленная для груза
 */
public record Shipment(
        List<Pack> packages, Currency currency,
        DestinationPoint destination, DeparturePoint departure) {
    public Shipment(List<Pack> packages, Currency currency,
                    DestinationPoint destination, DeparturePoint departure) {
        this.packages = packages;
        this.currency = currency;
        this.destination = destination;
        this.departure = departure;

        if (currency == null) {
            throw new IllegalArgumentException("Валюта должна быть указана");
        }
        if (packages == null) {
            throw new IllegalArgumentException("Список упаковок не может быть пустым");
        }
        if (packages().size() < 1) {
            throw new IllegalArgumentException("Упаковок не может быть меньше 1");
        }
    }
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

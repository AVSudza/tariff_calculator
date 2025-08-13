package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.fastdelivery.domain.delivery.coordinates.Coordinates;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.usecase.CoordinatesPropertiesProvider;

/**
 * Настройки координат пунктов доставки из конфига
 */
@Configuration
@ConfigurationProperties("coordinates-points")
@Setter
public class CoordinatesProperties implements CoordinatesPropertiesProvider {
    private Coordinates coordinates;

    @Override
    public boolean isPossible(Shipment shipment) {
        if (shipment.destination().latitude() == null) {
            throw new IllegalArgumentException("Latitude value destination is empty");
        }
        if (shipment.destination().longitude() == null) {
            throw new IllegalArgumentException("Longitude value destination is empty");
        }
        if (shipment.departure().latitude() == null) {
            throw new IllegalArgumentException("Latitude value departure is empty");
        }
        if (shipment.departure().longitude() == null) {
            throw new IllegalArgumentException("Longitude value departure is empty");
        }
        if (shipment.destination().latitude().compareTo(coordinates.getDestination().getLatitude().getMax()) > 0) {
            throw new IllegalArgumentException("Latitude value destination is higher than allowed");
        }
        if (shipment.destination().latitude().compareTo(coordinates.getDestination().getLatitude().getMin()) < 0) {
            throw new IllegalArgumentException("Latitude value destination is less than allowed");
        }
        if (shipment.destination().longitude().compareTo(coordinates.getDestination().getLongitude().getMax()) > 0) {
            throw new IllegalArgumentException("Longitude value destination is higher than allowed");
        }
        if (shipment.destination().longitude().compareTo(coordinates.getDestination().getLongitude().getMin()) < 0) {
            throw new IllegalArgumentException("Longitude value destination is less than allowed");
        }
        if (shipment.departure().latitude().compareTo(coordinates.getDeparture().getLatitude().getMax()) > 0) {
            throw new IllegalArgumentException("Latitude value departure is higher than allowed");
        }
        if (shipment.departure().latitude().compareTo(coordinates.getDeparture().getLatitude().getMin()) < 0) {
            throw new IllegalArgumentException("Latitude value departure is less than allowed");
        }
        if (shipment.departure().longitude().compareTo(coordinates.getDeparture().getLongitude().getMax()) > 0) {
            throw new IllegalArgumentException("Longitude value departure is higher than allowed");
        }
        if (shipment.departure().longitude().compareTo(coordinates.getDeparture().getLongitude().getMin()) < 0) {
            throw new IllegalArgumentException("Longitude value departure is less than allowed");
        }
        return true;
    }
}

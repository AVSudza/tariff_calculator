package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.fastdelivery.domain.delivery.coordinates.CoordinatesProperty;
import ru.fastdelivery.domain.delivery.points.CoordinatesPropertiesProvider;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

/**
 * Настройки координат пунктов доставки из конфига
 */
@Configuration
@ConfigurationProperties("coordinates-points")
@Setter
public class CoordinatesProperties implements CoordinatesPropertiesProvider {
    private CoordinatesProperty coordinates;

    public boolean isPossible(Shipment shipment) throws IllegalArgumentException {

        if (shipment.destination().getLatitude() == null) {
            throw new IllegalArgumentException("Latitude destination value is empty");
        }
        if (shipment.destination().getLongitude() == null) {
            throw new IllegalArgumentException("Longitude destination value is empty");
        }
        if (shipment.departure().getLatitude() == null) {
            throw new IllegalArgumentException("Latitude departure value is empty");
        }
        if (shipment.departure().getLongitude() == null) {
            throw new IllegalArgumentException("Longitude departure value is empty");
        }
        if (shipment.destination().getLatitude().compareTo(coordinates.getDestination().getLatitude().getMax()) > 0) {
            throw new IllegalArgumentException("Latitude destination value is higher than allowed");
        }
        if (shipment.destination().getLatitude().compareTo(coordinates.getDestination().getLatitude().getMin()) < 0) {
            throw new IllegalArgumentException("Latitude destination value is less than allowed");
        }
        if (shipment.destination().getLongitude().compareTo(coordinates.getDestination().getLongitude().getMax()) > 0) {
            throw new IllegalArgumentException("Longitude destination value is higher than allowed");
        }
        if (shipment.destination().getLongitude().compareTo(coordinates.getDestination().getLongitude().getMin()) < 0) {
            throw new IllegalArgumentException("Longitude destination value is less than allowed");
        }
        if (shipment.departure().getLatitude().compareTo(coordinates.getDeparture().getLatitude().getMax()) > 0) {
            throw new IllegalArgumentException("Latitude departure value is higher than allowed");
        }
        if (shipment.departure().getLatitude().compareTo(coordinates.getDeparture().getLatitude().getMin()) < 0) {
            throw new IllegalArgumentException("Latitude departure value is less than allowed");
        }
        if (shipment.departure().getLongitude().compareTo(coordinates.getDeparture().getLongitude().getMax()) > 0) {
            throw new IllegalArgumentException("Longitude departure value is higher than allowed");
        }
        if (shipment.departure().getLongitude().compareTo(coordinates.getDeparture().getLongitude().getMin()) < 0) {
            throw new IllegalArgumentException("Longitude departure value is less than allowed");
        }
        return true;
    }

    @Override
    public boolean isPossible() {
        return false;
    }

    @Override
    public CoordinatesProperty coordinates() {
        return coordinates;
    }

}

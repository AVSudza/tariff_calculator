package ru.fastdelivery.domain.delivery.points;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@RequiredArgsConstructor
public class CoordinatePoint {
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public boolean isPossible(CoordinatesPropertiesProvider coordinatesPropertiesProvider, String point) throws IllegalArgumentException {
        var coordinates = coordinatesPropertiesProvider.coordinates();

        if (point.equals("departure")) {
            if (latitude == null) {
                throw new IllegalArgumentException("Latitude departure value is empty");
            }
            if (longitude == null) {
                throw new IllegalArgumentException("Longitude departure value is empty");
            }
            if (latitude.compareTo(coordinates.getDeparture().getLatitude().getMax()) > 0) {
                throw new IllegalArgumentException("Latitude departure value is higher than allowed");
            }
            if (latitude.compareTo(coordinates.getDeparture().getLatitude().getMin()) < 0) {
                throw new IllegalArgumentException("Latitude departure value is less than allowed");
            }
            if (longitude.compareTo(coordinates.getDeparture().getLongitude().getMax()) > 0) {
                throw new IllegalArgumentException("Longitude departure value is higher than allowed");
            }
            if (longitude.compareTo(coordinates.getDeparture().getLongitude().getMin()) < 0) {
                throw new IllegalArgumentException("Longitude departure value is less than allowed");
            }
        }

        if (point.equals("destination")) {
            if (latitude == null) {
                throw new IllegalArgumentException("Latitude destination value is empty");
            }
            if (longitude == null) {
                throw new IllegalArgumentException("Longitude destination value is empty");
            }
            if (latitude.compareTo(coordinates.getDestination().getLatitude().getMax()) > 0) {
                throw new IllegalArgumentException("Latitude destination value is higher than allowed");
            }
            if (latitude.compareTo(coordinates.getDestination().getLatitude().getMin()) < 0) {
                throw new IllegalArgumentException("Latitude destination value is less than allowed");
            }
            if (longitude.compareTo(coordinates.getDestination().getLongitude().getMax()) > 0) {
                throw new IllegalArgumentException("Longitude destination value is higher than allowed");
            }
            if (longitude.compareTo(coordinates.getDestination().getLongitude().getMin()) < 0) {
                throw new IllegalArgumentException("Longitude destination value is less than allowed");
            }

        }
        return true;
    }
}

package ru.fastdelivery.domain.delivery.coordinates;

import jakarta.validation.constraints.NotBlank;

public class DepartureProperty extends DeliveryCoordinatesProperty {
    public DepartureProperty(@NotBlank LatitudeProperty latitude, @NotBlank LongitudeProperty longitude) {
        super(latitude, longitude);
    }
}

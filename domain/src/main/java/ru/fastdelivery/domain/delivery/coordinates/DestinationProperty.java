package ru.fastdelivery.domain.delivery.coordinates;

import jakarta.validation.constraints.NotBlank;

public class DestinationProperty extends DeliveryCoordinatesProperty {
    public DestinationProperty(@NotBlank LatitudeProperty latitude, @NotBlank LongitudeProperty longitude) {
        super(latitude, longitude);
    }
}

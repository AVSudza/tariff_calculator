package ru.fastdelivery.domain.delivery.coordinates;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class DeliveryCoordinatesProperty {
    @NotBlank
    private LatitudeProperty latitude;
    @NotBlank
    private LongitudeProperty longitude;
}

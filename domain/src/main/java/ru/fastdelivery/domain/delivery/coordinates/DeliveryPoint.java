package ru.fastdelivery.domain.delivery.coordinates;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DeliveryPoint {
    @NotBlank
    private Latitude latitude;
    @NotBlank
    private Longitude longitude;
}

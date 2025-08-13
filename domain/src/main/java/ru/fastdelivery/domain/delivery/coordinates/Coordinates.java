package ru.fastdelivery.domain.delivery.coordinates;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */
@Setter
@Getter
@ToString
public class Coordinates {
    Departure departure;
    Destination destination;
}

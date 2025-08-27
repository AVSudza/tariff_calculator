package ru.fastdelivery.domain.delivery.coordinates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class CoordinatesProperty {
    DepartureProperty departure;
    DestinationProperty destination;
}

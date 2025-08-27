package ru.fastdelivery.properties_provider;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.height.Height;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.common.width.Width;

import ru.fastdelivery.domain.delivery.coordinates.*;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.points.DeparturePoint;
import ru.fastdelivery.domain.delivery.points.DestinationPoint;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.properties.provider.CoordinatesProperties;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class CoordinatesPropertiesTest {
    public static final String RUB = "RUB";
    final CurrencyFactory currencyFactory = mock(CurrencyFactory.class);
    final Currency currency = mock(Currency.class);

    CoordinatesProperties properties = new CoordinatesProperties();
    Pack pack = new Pack(new Weight(BigInteger.valueOf(100_000), 150_000),
            new Length(BigInteger.valueOf(1_000), 1_500),
            new Width(BigInteger.valueOf(1_000), 1_500),
            new Height(BigInteger.valueOf(1_000), 1_500),
            BigInteger.valueOf(50));

    @BeforeEach
    void init() {
        CoordinatesProperty coordinates = new CoordinatesProperty(new DepartureProperty(
                new LatitudeProperty(BigDecimal.valueOf(45), BigDecimal.valueOf(65)),
                new LongitudeProperty(BigDecimal.valueOf(30), BigDecimal.valueOf(96))),
                new DestinationProperty(new LatitudeProperty(BigDecimal.valueOf(45), BigDecimal.valueOf(65)),
                new LongitudeProperty(BigDecimal.valueOf(30), BigDecimal.valueOf(96))));
        properties.setCoordinates(coordinates);

        when(currency.getCode()).thenReturn(RUB);
        when(currencyFactory.create(RUB)).thenReturn(currency);
    }

    @Test
    @DisplayName("Если отгрузка валидная -> true")
    void whenShipmentValid_thenReturnTrue() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(50), BigDecimal.valueOf(50));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(55), BigDecimal.valueOf(55));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        assertTrue(properties.isPossible(shipment));
    }

    @Test
    @DisplayName("Если широта пукта назначения null -> throw с сообщением")
    void whenDestinationLatitudeNull_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(null, BigDecimal.valueOf(50));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(55), BigDecimal.valueOf(55));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Latitude destination value is empty");
    }

    @Test
    @DisplayName("Если долгота пукта назначения null -> throw с сообщением")
    void whenDestinationLongitudeNull_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(50), null);
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(55), BigDecimal.valueOf(55));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Longitude destination value is empty");
    }

    @Test
    @DisplayName("Если широта пукта отправления null -> throw с сообщением")
    void whenDepartureLatitudeNull_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(55), BigDecimal.valueOf(50));
        DeparturePoint departurePoint = new DeparturePoint(null, BigDecimal.valueOf(55));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Latitude departure value is empty");
    }

    @Test
    @DisplayName("Если долгота пукта отправления null -> throw с сообщением")
    void whenDepartureLongitudeNull_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(50), BigDecimal.valueOf(55));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(55), null);
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Longitude departure value is empty");
    }


    @Test
    @DisplayName("Если широта пукта назначения меньше допустимой -> throw с сообщением")
    void whenDestinationLatitudeLessThanAllowed_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(20), BigDecimal.valueOf(50));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(55), BigDecimal.valueOf(55));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Latitude destination value is less than allowed");
    }

    @Test
    @DisplayName("Если долгота пукта назначения меньше допустимой -> throw с сообщением")
    void whenDestinationLongitudeLessThanAllowed_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(50), BigDecimal.valueOf(20));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(55), BigDecimal.valueOf(55));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Longitude destination value is less than allowed");
    }

    @Test
    @DisplayName("Если широта пукта отправления меньше допустимой -> throw с сообщением")
    void whenDepartureLatitudeLessThanAllowed_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(55), BigDecimal.valueOf(50));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(20), BigDecimal.valueOf(55));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Latitude departure value is less than allowed");
    }

    @Test
    @DisplayName("Если долгота пукта отправления меньше допустимой -> throw с сообщением")
    void whenDepartureLongitudeLessThanAllowed_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(50), BigDecimal.valueOf(55));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(55), BigDecimal.valueOf(20));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Longitude departure value is less than allowed");
    }

    @Test
    @DisplayName("Если широта пукта назначения больше допустимой -> throw с сообщением")
    void whenDestinationLatitudeMoreThanAllowed_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(200), BigDecimal.valueOf(50));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(55), BigDecimal.valueOf(55));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Latitude destination value is higher than allowed");
    }

    @Test
    @DisplayName("Если долгота пукта назначения больше допустимой -> throw с сообщением")
    void whenDestinationLongitudeMoreThanAllowed_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(50), BigDecimal.valueOf(200));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(55), BigDecimal.valueOf(55));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Longitude destination value is higher than allowed");
    }

    @Test
    @DisplayName("Если широта пукта отправления больше допустимой -> throw с сообщением")
    void whenDepartureLatitudeMoreThanAllowed_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(55), BigDecimal.valueOf(50));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(200), BigDecimal.valueOf(55));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Latitude departure value is higher than allowed");
    }

    @Test
    @DisplayName("Если долгота пукта отправления больше допустимой -> throw с сообщением")
    void whenDepartureLongitudeMoreThanAllowed_thenThrows() {
        DestinationPoint destinationPoint = new DestinationPoint(BigDecimal.valueOf(50), BigDecimal.valueOf(55));
        DeparturePoint departurePoint = new DeparturePoint(BigDecimal.valueOf(55), BigDecimal.valueOf(200));
        Shipment shipment = new Shipment(List.of(pack),
                currencyFactory.create("RUB"),
                destinationPoint, departurePoint);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> properties.isPossible(shipment));
        assertEquals(thrown.getMessage(), "Longitude departure value is higher than allowed");
    }

}

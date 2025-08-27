package ru.fastdelivery.calc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.fastdelivery.ControllerTest;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.height.HeightPropertiesProvider;
import ru.fastdelivery.domain.common.length.LengthPropertiesProvider;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.stepNormalize.StepNormalizeProvider;
import ru.fastdelivery.domain.common.weight.WeightPropertiesProvider;
import ru.fastdelivery.domain.common.width.WidthPropertiesProvider;
import ru.fastdelivery.domain.delivery.coordinates.*;
import ru.fastdelivery.domain.delivery.points.*;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.request.CargoPackage;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CalculateControllerTest extends ControllerTest {

    final String baseCalculateApi = "/api/v1/calculate/";
    @MockBean
    TariffCalculateUseCase tariffCalculateUseCase;
    @MockBean
    CurrencyFactory currencyFactory;
    @MockBean
    WeightPropertiesProvider weightPropertiesProvider;
    @MockBean
    LengthPropertiesProvider lengthPropertiesProvider;
    @MockBean
    WidthPropertiesProvider widthPropertiesProvider;
    @MockBean
    HeightPropertiesProvider heightPropertiesProvider;
    @MockBean
    StepNormalizeProvider stepNormalizeProvider;
    @MockBean
    CoordinatesPropertiesProvider coordinatesPropertiesProvider;

    public static final String RUB = "RUB";
    public static final int MAX_WEIGHT = 150_000;
    public static final int MAX_LENGTH = 1500;
    public static final int MAX_WIDTH = 1500;
    public static final int MAX_HEIGHT = 1500;

    @BeforeEach
    void init() {
        when(weightPropertiesProvider.getMax()).thenReturn(MAX_WEIGHT);
        when(lengthPropertiesProvider.getMax()).thenReturn(MAX_LENGTH);
        when(widthPropertiesProvider.getMax()).thenReturn(MAX_WIDTH);
        when(heightPropertiesProvider.getMax()).thenReturn(MAX_HEIGHT);
        when(stepNormalizeProvider.getStepNormalize()).thenReturn(50);
        var currency = mock(Currency.class);

        when(currency.getCode()).thenReturn(RUB);
        when(currencyFactory.create(RUB)).thenReturn(currency);
        var rub = new CurrencyFactory(code -> true).create("RUB");
        when(tariffCalculateUseCase.calc(any())).thenReturn(new Price(BigDecimal.valueOf(10), rub));
        when(tariffCalculateUseCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(5), rub));
        CoordinatesProperty coordinatesProperty = new CoordinatesProperty(
                new DepartureProperty(
                        new LatitudeProperty(BigDecimal.valueOf(45), BigDecimal.valueOf(65)),
                        new LongitudeProperty(BigDecimal.valueOf(30), BigDecimal.valueOf(96))
                ),
                new DestinationProperty(
                        new LatitudeProperty(BigDecimal.valueOf(45), BigDecimal.valueOf(65)),
                        new LongitudeProperty(BigDecimal.valueOf(30), BigDecimal.valueOf(96))
                )
        );
        when(coordinatesPropertiesProvider.coordinates()).thenReturn(coordinatesProperty);

    }

    @Test
    @DisplayName("Валидные данные для расчета стоимость -> Ответ 200")
    void whenValidInputData_thenReturn200() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<CalculatePackagesResponse> response =
                restTemplate.postForEntity(baseCalculateApi, request, CalculatePackagesResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Длина < 0 -> Ответ 400")
    void whenNegativeLength_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.valueOf(-10), BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Length cannot be below Zero!\"}", response.getBody());
    }

    @Test
    @DisplayName("Ширина < 0 -> Ответ 400")
    void whenNegativeWidth_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.valueOf(-10), BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Width cannot be below Zero!\"}", response.getBody());
    }

    @Test
    @DisplayName("Высота < 0 -> Ответ 400")
    void whenNegativeHeight_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(-10))),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Height cannot be below Zero!\"}", response.getBody());
    }

    @Test
    @DisplayName("Длина > 1500мм -> Ответ 400")
    void whenLengthMoreThan1500mm_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.valueOf(1501), BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Length cannot be higher than " + MAX_LENGTH + "!\"}", response.getBody());
    }

    @Test
    @DisplayName("Ширина > 1500мм -> Ответ 400")
    void whenWidthMoreThan1500mm_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.valueOf(1501), BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        var rub = new CurrencyFactory(code -> true).create("RUB");
        when(tariffCalculateUseCase.calc(any())).thenReturn(new Price(BigDecimal.valueOf(10), rub));
        when(tariffCalculateUseCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(5), rub));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Width cannot be higher than " + MAX_WIDTH + "!\"}", response.getBody());
    }

    @Test
    @DisplayName("Высота > 1500мм -> Ответ 400")
    void whenHeightMoreThan1500mm_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(1501))),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Height cannot be higher than " + MAX_HEIGHT + "!\"}", response.getBody());
    }

    @Test
    @DisplayName("Широта пункта назначения больше максимальной -> Ответ 400")
    void whenDestinationLatitudeMoreThanMax_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(99.999999), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));


        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Latitude destination value is higher than allowed\"}", response.getBody());
    }


    @Test
    @DisplayName("Долгота пункта назначения больше максимальной -> Ответ 400")
    void whenDestinationLongitudeMoreThanMax_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(99.999999)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));


        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Longitude destination value is higher than allowed\"}", response.getBody());
    }

    @Test
    @DisplayName("Широта пункта отправления больше максимальной -> Ответ 400")
    void whenDepartureLatitudeMoreThanMax_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(99.999999), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Latitude departure value is higher than allowed\"}", response.getBody());
    }


    @Test
    @DisplayName("Долгота пункта отправления больше максимальной -> Ответ 400")
    void whenDepartureLongitudeMoreThanMax_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(99.999999)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Longitude departure value is higher than allowed\"}", response.getBody());
    }

    @Test
    @DisplayName("Широта пункта назначения меньше минимальной -> Ответ 400")
    void whenDestinationLatitudeLessThanMin_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(11.111111), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));


        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Latitude destination value is less than allowed\"}", response.getBody());
    }


    @Test
    @DisplayName("Долгота пункта назначения меньше минимальной -> Ответ 400")
    void whenDestinationLongitudeLessThanMin_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(11.111111)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));


        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Longitude destination value is less than allowed\"}", response.getBody());
    }

    @Test
    @DisplayName("Широта пункта отправления меньше минимальной -> Ответ 400")
    void whenDepartureLatitudeLessThanMin_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(11.111111), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Latitude departure value is less than allowed\"}", response.getBody());
    }


    @Test
    @DisplayName("Долгота пункта отправления больше максимальной -> Ответ 400")
    void whenDepartureLongitudeLessThanMin_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(11.111111)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"status\":\"error\",\"message\":\"Longitude departure value is less than allowed\"}", response.getBody());
    }

    @Test
    @DisplayName("Список упаковок == null -> Ответ 400")
    void whenEmptyListPackages_thenReturn400() {
        var request = new CalculatePackagesRequest(null, "RUB",
                new DestinationPoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePoint(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

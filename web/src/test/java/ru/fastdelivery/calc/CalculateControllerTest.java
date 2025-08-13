package ru.fastdelivery.calc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.fastdelivery.ControllerTest;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.height.HeightPropertiesProvider;
import ru.fastdelivery.domain.common.length.LengthPropertiesProvider;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.stepNormalize.StepNormalizeProvider;
import ru.fastdelivery.domain.common.weight.WeightPropertiesProvider;
import ru.fastdelivery.domain.common.width.WidthPropertiesProvider;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.request.CargoPackage;
import ru.fastdelivery.domain.delivery.points.Departure;
import ru.fastdelivery.domain.delivery.points.Destination;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculateControllerTest extends ControllerTest {

    final String baseCalculateApi = "/api/v1/calculate/";
    @MockBean
    TariffCalculateUseCase useCase;
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


    @Test
    @DisplayName("Валидные данные для расчета стоимость -> Ответ 200")
    void whenValidInputData_thenReturn200() {
        when(weightPropertiesProvider.getMax()).thenReturn(150000);
        when(lengthPropertiesProvider.getMax()).thenReturn(1500000);
        when(widthPropertiesProvider.getMax()).thenReturn(1500000);
        when(heightPropertiesProvider.getMax()).thenReturn(1500000);
        when(stepNormalizeProvider.get()).thenReturn(50);
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE)),
                "RUB", new Destination(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new Departure(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));
        var rub = new CurrencyFactory(code -> true).create("RUB");
        when(useCase.calc(any())).thenReturn(new Price(BigDecimal.valueOf(10), rub));
        when(useCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(5), rub));

        ResponseEntity<CalculatePackagesResponse> response =
                restTemplate.postForEntity(baseCalculateApi, request, CalculatePackagesResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Длина < 0 -> Ответ 400")
    void whenNegativeLength_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.valueOf(-10), BigInteger.ONE, BigInteger.ONE)),
                "RUB", new Destination(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new Departure(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Ширина < 0 -> Ответ 400")
    void whenNegativeWidth_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.valueOf(-10), BigInteger.ONE)),
                "RUB", new Destination(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new Departure(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Высота < 0 -> Ответ 400")
    void whenNegativeHeight_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(-10))),
                "RUB", new Destination(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new Departure(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Длина > 1500мм -> Ответ 400")
    void whenLengthMoreThan1500mm_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.valueOf(1501), BigInteger.ONE, BigInteger.ONE)),
                "RUB", new Destination(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new Departure(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Ширина > 1500мм -> Ответ 400")
    void whenWidthMoreThan1500mm_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.valueOf(1501), BigInteger.ONE)),
                "RUB", new Destination(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new Departure(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Высота > 1500мм -> Ответ 400")
    void whenHeightMoreThan1500mm_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(1501))),
                "RUB", new Destination(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new Departure(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Список упаковок == null -> Ответ 400")
    void whenEmptyListPackages_thenReturn400() {
        var request = new CalculatePackagesRequest(null, "RUB",
                new Destination(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new Departure(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

package ru.fastdelivery.usecase;

import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.distanceBaseCost.DistanceBaseCostProvider;
import ru.fastdelivery.domain.common.height.Height;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.common.weight.WeightPropertiesProvider;
import ru.fastdelivery.domain.common.width.Width;
import ru.fastdelivery.domain.delivery.coordinates.*;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.points.*;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TariffCalculateUseCaseTest {

    final WeightPriceProvider weightPriceProvider = mock(WeightPriceProvider.class);
    final WeightPropertiesProvider weightPropertiesProvider = mock(WeightPropertiesProvider.class);
    final DistanceBaseCostProvider distanceBaseCostProvider = mock(DistanceBaseCostProvider.class);
    final Currency currency = new CurrencyFactory(code -> true).create("RUB");

    final TariffCalculateUseCase tariffCalculateUseCase = new TariffCalculateUseCase(weightPriceProvider,
            distanceBaseCostProvider);

    final Length lengthOne = new Length(BigInteger.ONE, Integer.MAX_VALUE);
    final Width widthOne = new Width(BigInteger.ONE);
    final Height heightOne = new Height(BigInteger.ONE, Integer.MAX_VALUE);
    static CoordinatesPropertiesProvider coordinatesProvider = Mockito.mock(CoordinatesPropertiesProvider.class);

    @BeforeAll
    static void init() {
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
        when(coordinatesProvider.coordinates()).thenReturn(coordinatesProperty);

    }

    @Test
    @DisplayName("Расчет стоимости доставки по весу -> успешно")
    void whenCalculateWeightPrice_thenSuccess() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerCubicMeter = new Price(BigDecimal.valueOf(10000), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(pricePerCubicMeter);
        when(weightPropertiesProvider.getMax()).thenReturn(150000);

        var shipment = new Shipment(List.of(
                new Pack(new Weight(BigInteger.valueOf(1200), weightPropertiesProvider.getMax()),
                        lengthOne, widthOne, heightOne, BigInteger.valueOf(50))),
                new CurrencyFactory(code -> true).create("RUB"),
                new DestinationPointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));
        var expectedPrice = new Price(BigDecimal.valueOf(120), currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Расчет стоимости доставки по объему -> успешно")
    void whenCalculateVolumePrice_thenSuccess() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerCubicMeter = new Price(BigDecimal.valueOf(12000000), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(pricePerCubicMeter);
        when(weightPropertiesProvider.getMax()).thenReturn(150000);

        var shipment = new Shipment(
                List.of(new Pack(new Weight(BigInteger.valueOf(1200), weightPropertiesProvider.getMax()),
                        lengthOne, widthOne, heightOne, BigInteger.valueOf(50))),
                new CurrencyFactory(code -> true).create("RUB"),
                new DestinationPointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));
        var expectedPrice = new Price(BigDecimal.valueOf(1200), currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Расчет стоимости доставки по расстоянию -> успешно")
    void whenCalculateDistancePrice_thenSuccess() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerCubicMeter = new Price(BigDecimal.valueOf(12000000), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(pricePerCubicMeter);
        when(weightPropertiesProvider.getMax()).thenReturn(150000);
        when(distanceBaseCostProvider.getDistanceBaseCost()).thenReturn(450);
        var shipment = new Shipment(
                List.of(new Pack(new Weight(BigInteger.valueOf(1200), weightPropertiesProvider.getMax()),
                        lengthOne, widthOne, heightOne, BigInteger.valueOf(50))),
                new CurrencyFactory(code -> true).create("RUB"),
                new DestinationPointFactory(coordinatesProvider)
                        .create(BigDecimal.valueOf(50.1539), BigDecimal.valueOf(58.398)),
                new DeparturePointFactory(coordinatesProvider)
                        .create(BigDecimal.valueOf(54.508), BigDecimal.valueOf(56.55)));
        var expectedPrice = new Price(BigDecimal.valueOf(1333.34), currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Получение минимальной стоимости -> успешно")
    void whenMinimalPrice_thenSuccess() {
        BigDecimal minimalValue = BigDecimal.TEN;
        var minimalPrice = new Price(minimalValue, currency);
        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);

        var actual = tariffCalculateUseCase.minimalPrice();

        assertThat(actual).isEqualTo(minimalPrice);
    }
}
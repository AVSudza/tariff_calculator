package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.height.Height;
import ru.fastdelivery.domain.common.height.HeightPropertiesProvider;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.length.LengthPropertiesProvider;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.common.weight.WeightPropertiesProvider;
import ru.fastdelivery.domain.common.width.Width;
import ru.fastdelivery.domain.common.width.WidthPropertiesProvider;
import ru.fastdelivery.domain.delivery.coordinates.*;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.points.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ShipmentTest {

    Length lengthZero = new Length(BigInteger.ZERO, Integer.MAX_VALUE);
    Width widthZero = new Width(BigInteger.ZERO);
    Height heightZero = new Height(BigInteger.ZERO, Integer.MAX_VALUE);
    BigInteger stepNormalize = BigInteger.valueOf(50);
    static WeightPropertiesProvider weightPropertiesProvider = Mockito.mock(WeightPropertiesProvider.class);
    static LengthPropertiesProvider lengthPropertiesProvider = Mockito.mock(LengthPropertiesProvider.class);
    static WidthPropertiesProvider widthPropertiesProvider = Mockito.mock(WidthPropertiesProvider.class);
    static HeightPropertiesProvider heightPropertiesProvider = Mockito.mock(HeightPropertiesProvider.class);
    static CoordinatesPropertiesProvider coordinatesProvider = Mockito.mock(CoordinatesPropertiesProvider.class);

    @BeforeAll
    static void init() {
        when(weightPropertiesProvider.getMax()).thenReturn(150000);
        when(lengthPropertiesProvider.getMax()).thenReturn(1500);
        when(widthPropertiesProvider.getMax()).thenReturn(1500);
        when(heightPropertiesProvider.getMax()).thenReturn(1500);
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
    @DisplayName("Суммирование веса всех пакетов -> сумма весов пакетов")
    void whenSummarizingWeightOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN, weightPropertiesProvider.getMax());
        var weight2 = new Weight(BigInteger.ONE, weightPropertiesProvider.getMax());

        var packages = List.of(new Pack(weight1, lengthZero, widthZero, heightZero, stepNormalize),
                new Pack(weight2, lengthZero, widthZero, heightZero, stepNormalize));
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"),
                new DestinationPointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        var massOfShipment = shipment.weightAllPackages();

        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(11));
    }

    @Test
    @DisplayName("Список пакетов null -> ошибка")
    void whenPackagesNull_thenException() {
        assertThatThrownBy(() -> new Shipment(null, new CurrencyFactory(code -> true).create("RUB"),
                new DestinationPointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Колличество пакетов меньше 1 -> ошибка")
    void whenPackagesCountLessOne_thenException() {
        assertThatThrownBy(() -> new Shipment(List.of(), new CurrencyFactory(code -> true).create("RUB"),
                new DestinationPointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Валюта null -> ошибка")
    void whenCurrencyNull_thenException() {
        var weight1 = new Weight(BigInteger.TEN, weightPropertiesProvider.getMax());
        var weight2 = new Weight(BigInteger.ONE, weightPropertiesProvider.getMax());
        var packages = List.of(new Pack(weight1, lengthZero, widthZero, heightZero, stepNormalize),
                new Pack(weight2, lengthZero, widthZero, heightZero, stepNormalize));
        assertThatThrownBy(() -> new Shipment(packages, null,
                new DestinationPointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Список пакетов -> сумма размеров всех пакетов")
    void whenListPackages_thenSummationDimensionsAllPackages () {
        BigInteger fifty = BigInteger.valueOf(50);
        BigInteger hundred = BigInteger.valueOf(100);
        BigInteger twoHundred = BigInteger.valueOf(200);
        var weight = new Weight(BigInteger.TEN, weightPropertiesProvider.getMax());
        var length1 = new Length(fifty, lengthPropertiesProvider.getMax());
        var width1 = new Width(fifty, widthPropertiesProvider.getMax());
        var height1 = new Height(fifty, heightPropertiesProvider.getMax());
        var length2 = new Length(hundred, lengthPropertiesProvider.getMax());
        var width2 = new Width(hundred, widthPropertiesProvider.getMax());
        var height2 = new Height(hundred, heightPropertiesProvider.getMax());
        var length3 = new Length(twoHundred, lengthPropertiesProvider.getMax());
        var width3 = new Width(twoHundred, widthPropertiesProvider.getMax());
        var height3 = new Height(twoHundred, heightPropertiesProvider.getMax());

        var packages = List.of(new Pack(weight, length1, width1, height1, stepNormalize),
                new Pack(weight, length2, width2, height2, stepNormalize),
                new Pack(weight, length3, width3, height3, stepNormalize));
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"),
                new DestinationPointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new DeparturePointFactory(coordinatesProvider).create(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        var volumeOfShipment = shipment.volumeAllPackages();

        assertThat(volumeOfShipment.volumeCubicMills()).isEqualByComparingTo(BigInteger.valueOf(9125000));

    }

}
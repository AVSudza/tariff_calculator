package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.height.Height;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.common.weight.WeightPropertiesProvider;
import ru.fastdelivery.domain.common.width.Width;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.points.Departure;
import ru.fastdelivery.domain.delivery.points.Destination;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ShipmentTest {

    Length lengthZero = new Length(BigInteger.ZERO, Integer.MAX_VALUE);
    Width widthZero = new Width(BigInteger.ZERO);
    Height heightZero = new Height(BigInteger.ZERO, Integer.MAX_VALUE);
    BigInteger stepNormalize = BigInteger.valueOf(50);
    static WeightPropertiesProvider weightPropertiesProvider = Mockito.mock(WeightPropertiesProvider.class);

    @BeforeAll
    static void init() {
        when(weightPropertiesProvider.getMax()).thenReturn(150000);
    }

    @Test
    void whenSummarizingWeightOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN, weightPropertiesProvider.getMax());
        var weight2 = new Weight(BigInteger.ONE, weightPropertiesProvider.getMax());

        var packages = List.of(new Pack(weight1, lengthZero, widthZero, heightZero, stepNormalize),
                new Pack(weight2, lengthZero, widthZero, heightZero, stepNormalize));
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"),
                new Destination(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)),
                new Departure(BigDecimal.valueOf(55.555555), BigDecimal.valueOf(33.333333)));

        var massOfShipment = shipment.weightAllPackages();

        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(11));
    }
}
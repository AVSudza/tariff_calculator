package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.fastdelivery.domain.common.height.Height;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.common.weight.WeightPropertiesProvider;
import ru.fastdelivery.domain.common.width.Width;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class PackTest {

    Length lengthZero = new Length(BigInteger.ZERO);
    Width widthZero = new Width(BigInteger.ZERO);
    Height heightZero = new Height(BigInteger.ZERO);
    @Mock
    static WeightPropertiesProvider weightPropertiesProvider = Mockito.mock(WeightPropertiesProvider.class);

    @BeforeAll
    static void init() {
        when(weightPropertiesProvider.getMax()).thenReturn(150000);
    }

    @Test
    void whenWeightMoreThanMaxWeight_thenThrowException() {
        assertThatThrownBy(() -> new Pack(
                new Weight(BigInteger.valueOf(150_001), weightPropertiesProvider.getMax()),
                lengthZero, widthZero, heightZero, BigInteger.valueOf(50)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenWeightLessThanMaxWeight_thenObjectCreated() {
        assertThat(new Pack(
                new Weight(BigInteger.valueOf(1_000), weightPropertiesProvider.getMax()),
                lengthZero, widthZero, heightZero, BigInteger.valueOf(50)).weight())
                .isEqualTo(new Weight(BigInteger.valueOf(1_000), weightPropertiesProvider.getMax()));
    }

    @Test
    void whenCalculatingVolume_thenDimensionsNormalized() {
        Pack pack = new Pack(new Weight(BigInteger.valueOf(4564), weightPropertiesProvider.getMax()),
                new Length(BigInteger.valueOf(345)),
                new Width(BigInteger.valueOf(589)),
                new Height(BigInteger.valueOf(234)),
                BigInteger.valueOf(50));
        var actual = pack.getNormalizeVolume().cubicMeters();
        assertThat(actual).isEqualTo(BigDecimal.valueOf(0.0525));
    }
}
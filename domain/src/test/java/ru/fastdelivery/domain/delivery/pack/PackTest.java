package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.height.Height;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.common.width.Width;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PackTest {

    Length lengthZero = new Length(BigInteger.ZERO);
    Width widthZero = new Width(BigInteger.ZERO);
    Height heightZero = new Height(BigInteger.ZERO);

    @Test
    void whenWeightMoreThanMaxWeight_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(150_001));
        assertThatThrownBy(() -> new Pack(weight, lengthZero, widthZero, heightZero))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenWeightLessThanMaxWeight_thenObjectCreated() {
        var actual = new Pack(new Weight(BigInteger.valueOf(1_000)), lengthZero, widthZero, heightZero);
        assertThat(actual.weight()).isEqualTo(new Weight(BigInteger.valueOf(1_000)));
    }

    @Test
    void whenCalculatingVolume_thenDimensionsNormalized() {
        Pack pack = new Pack( new Weight(BigInteger.valueOf(4564)),
                new Length(BigInteger.valueOf(345)) ,
                new Width(BigInteger.valueOf(589)),
                new Height(BigInteger.valueOf(234)));
        var actual = pack.getNormalizeVolume().cubicMeters();
        assertThat(actual).isEqualTo(BigDecimal.valueOf(0.0525));
    }
}
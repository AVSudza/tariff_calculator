package ru.fastdelivery.domain.common.weight;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class WeightTest {

    static WeightPropertiesProvider weightPropertiesProvider = Mockito.mock(WeightPropertiesProvider.class);

    @BeforeAll
    static void init() {
        when(weightPropertiesProvider.getMax()).thenReturn(150000);
    }

    @Test
    @DisplayName("Попытка создать отрицательный вес -> исключение")
    void whenGramsBelowZero_thenException() {
        var weightGrams = new BigInteger("-1");
        assertThatThrownBy(() -> new Weight(weightGrams, weightPropertiesProvider.getMax()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void equalsTypeWidth_same() {
        var weight = new Weight(new BigInteger("1000"), weightPropertiesProvider.getMax());
        var weightSame = new Weight(new BigInteger("1000"), weightPropertiesProvider.getMax());

        assertThat(weight)
                .isEqualTo(weightSame)
                .hasSameHashCodeAs(weightSame);
    }

    @Test
    void equalsNull_false() {
        var weight = new Weight(new BigInteger("4"), weightPropertiesProvider.getMax());

        assertThat(weight).isNotEqualTo(null);
    }

    @ParameterizedTest
    @CsvSource({ "1000, 1, -1",
            "199, 199, 0",
            "50, 999, 1" })
    void compareToTest(BigInteger low, BigInteger high, int expected) {
        var weightLow = new Weight(low, weightPropertiesProvider.getMax());
        var weightHigh = new Weight(high, weightPropertiesProvider.getMax());

        assertThat(weightLow.compareTo(weightHigh))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Добавление положительного веса -> вес увеличился")
    void whenAddPositiveWeight_thenWeightIsIncreased() {
        var weightBase = new Weight(new BigInteger("1000"), weightPropertiesProvider.getMax());
        var actual = weightBase.add(new Weight(new BigInteger("1000"), weightPropertiesProvider.getMax()));

        assertThat(actual)
                .isEqualTo(new Weight(new BigInteger("2000"), weightPropertiesProvider.getMax()));
    }

    @Test
    @DisplayName("Первый вес больше второго -> true")
    void whenFirstWeightGreaterThanSecond_thenTrue() {
        var weightBig = new Weight(new BigInteger("1001"), weightPropertiesProvider.getMax());
        var weightSmall = new Weight(new BigInteger("1000"), weightPropertiesProvider.getMax());

        assertThat(weightBig.greaterThan(weightSmall)).isTrue();
    }

    @Test
    @DisplayName("Запрос количество кг -> получено корректное значение")
    void whenGetKilograms_thenReceiveKg() {
        var weight = new Weight(new BigInteger("1001"), weightPropertiesProvider.getMax());

        var actual = weight.kilograms();

        assertThat(actual).isEqualByComparingTo(new BigDecimal("1.001"));
    }
}
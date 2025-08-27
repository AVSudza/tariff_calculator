package ru.fastdelivery.domain.common.weight;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class WeightFactoryTest {

    static WeightPropertiesProvider weightPropertiesProvider = Mockito.mock(WeightPropertiesProvider.class);

    @BeforeAll
    static void init() {
        when(weightPropertiesProvider.getMax()).thenReturn(150000);
    }

    @ParameterizedTest(name = "Граммы = {arguments} -> объект создан")
    @ValueSource(longs = { 0, 1, 100, 10_000 })
    void whenGramsGreaterThanZero_thenObjectCreated(long amount) {
        var weight = new Weight(BigInteger.valueOf(amount), weightPropertiesProvider.getMax());

        assertNotNull(weight);
        assertThat(weight.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(amount));
    }

    @ParameterizedTest(name = "Вес = {arguments} -> исключение")
    @ValueSource(longs = { -1, -100, -10_000 })
    @DisplayName("Значение веса ниже 0.00 -> исключение")
    void whenGramsLessThanZero_thenThrowException(long amount) {
        assertThatThrownBy(() -> new Weight(BigInteger.valueOf(amount), weightPropertiesProvider.getMax()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
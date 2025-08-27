package ru.fastdelivery.domain.common.height;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("Проверка создаваемой высоты")
public class HeightTest {

    static HeightPropertiesProvider heightPropertiesProvider = Mockito.mock(HeightPropertiesProvider.class);

    @BeforeAll
    static void init() {
        when(heightPropertiesProvider.getMax()).thenReturn(1500);
    }

    @Test
    @DisplayName("Попытка создать отрицательную высоту -> исключение")
    void whenGramsBelowZero_thenException() {
        var heightMills = new BigInteger("-1");
        assertThatThrownBy(() -> new Height(heightMills, heightPropertiesProvider.getMax()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Попытка создать высоту больше максимальной -> исключение")
    void whenGramsMoreMax_thenException() {
        var heightMills = new BigInteger("1501");
        assertThatThrownBy(() -> new Height(heightMills, heightPropertiesProvider.getMax()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Сравнение объектов высоты по хешкоду")
    void equalsTypeWidth_same() {
        var height = new Height(new BigInteger("1000"), heightPropertiesProvider.getMax());
        var heightSame = new Height(new BigInteger("1000"), heightPropertiesProvider.getMax());

        assertThat(height)
                .isEqualTo(heightSame)
                .hasSameHashCodeAs(heightSame);
    }

    @Test
    @DisplayName("Созданный объект высоты не null")
    void equalsNull_false() {
        var height = new Height(new BigInteger("4"), heightPropertiesProvider.getMax());

        assertThat(height).isNotEqualTo(null);
    }

}

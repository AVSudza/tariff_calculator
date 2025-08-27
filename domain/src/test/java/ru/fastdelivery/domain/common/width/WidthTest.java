package ru.fastdelivery.domain.common.width;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("Проверка создаваемой ширины")
public class WidthTest {

    static WidthPropertiesProvider widthPropertiesProvider = Mockito.mock(WidthPropertiesProvider.class);

    @BeforeAll
    static void init() {
        when(widthPropertiesProvider.getMax()).thenReturn(1500);
    }

    @Test
    @DisplayName("Попытка создать отрицательную ширину -> исключение")
    void whenGramsBelowZero_thenException() {
        var widthMills = new BigInteger("-1");
        assertThatThrownBy(() -> new Width(widthMills, widthPropertiesProvider.getMax()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Попытка создать ширину больше максимальной -> исключение")
    void whenGramsMoreMax_thenException() {
        var widthMills = new BigInteger("1501");
        assertThatThrownBy(() -> new Width(widthMills, widthPropertiesProvider.getMax()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Сравнение объектов ширины по хешкоду")
    void equalsTypeWidth_same() {
        var width = new Width(new BigInteger("1000"), widthPropertiesProvider.getMax());
        var widthSame = new Width(new BigInteger("1000"), widthPropertiesProvider.getMax());

        assertThat(width)
                .isEqualTo(widthSame)
                .hasSameHashCodeAs(widthSame);
    }

    @Test
    @DisplayName("Созданный объект ширины не null")
    void equalsNull_false() {
        var width = new Width(new BigInteger("4"), widthPropertiesProvider.getMax());

        assertThat(width).isNotEqualTo(null);
    }
}

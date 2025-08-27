package ru.fastdelivery.domain.common.length;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("Проверка создаваемой длины")
public class LengthTest {

    static LengthPropertiesProvider lengthPropertiesProvider = Mockito.mock(LengthPropertiesProvider.class);

    @BeforeAll
    static void init() {
        when(lengthPropertiesProvider.getMax()).thenReturn(1500);
    }

    @Test
    @DisplayName("Попытка создать отрицательную длину -> исключение")
    void whenGramsBelowZero_thenException() {
        var lengthMills = new BigInteger("-1");
        assertThatThrownBy(() -> new Length(lengthMills, lengthPropertiesProvider.getMax()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Попытка создать длину больше максимальной -> исключение")
    void whenGramsMoreMax_thenException() {
        var lengthMills = new BigInteger("1501");
        assertThatThrownBy(() -> new Length(lengthMills, lengthPropertiesProvider.getMax()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Сравнение объектов длины по хешкоду")
    void equalsTypeWidth_same() {
        var length = new Length(new BigInteger("1000"), lengthPropertiesProvider.getMax());
        var lengthSame = new Length(new BigInteger("1000"), lengthPropertiesProvider.getMax());

        assertThat(length)
                .isEqualTo(lengthSame)
                .hasSameHashCodeAs(lengthSame);
    }

    @Test
    @DisplayName("Созданный объект длину не null")
    void equalsNull_false() {
        var length = new Length(new BigInteger("4"), lengthPropertiesProvider.getMax());

        assertThat(length).isNotEqualTo(null);
    }
}

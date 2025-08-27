package ru.fastdelivery.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

public class HaversinesWithAntipodesTest {

    private final long EXPECTED_DISTANCE = 17166;
    private final long TEST_DEGREES = 180;
    private final int ACCURACY = 15;
    BigDecimal pointOneLat = BigDecimal.valueOf(77.1539);
    BigDecimal pointOneLong = BigDecimal.valueOf(-139.398);
    BigDecimal pointTwoLat = BigDecimal.valueOf(-77.1804);
    BigDecimal pointTwoLong  = BigDecimal.valueOf(-139.55);
    HaversinesWithAntipodes haversinesWithAntipodes = new HaversinesWithAntipodes(
            pointOneLat, pointOneLong, pointTwoLat, pointTwoLong);

    @Test
    @DisplayName("Расчёт расстояния между географическими координатами")
    void WhenDistance_thenSuccess() {
        BigDecimal expectedDistance = BigDecimal.valueOf(EXPECTED_DISTANCE);
        BigDecimal actualDistance = haversinesWithAntipodes.getDistance();
        assertThat(actualDistance).isEqualTo(expectedDistance);
    }

    @Test
    @DisplayName("Пересчет градусов в радианы")
    void WhenRadian_thenSuccess() {
        BigDecimal expectedRadians = BigDecimal.valueOf(3.141592653589793);
        BigDecimal actualRadians = haversinesWithAntipodes
                .degreesToRad(BigDecimal.valueOf(TEST_DEGREES))
                .setScale(ACCURACY, RoundingMode.HALF_UP);
        assertThat(actualRadians).isEqualTo(expectedRadians);
    }

    }

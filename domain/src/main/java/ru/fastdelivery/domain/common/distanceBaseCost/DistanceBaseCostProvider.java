package ru.fastdelivery.domain.common.distanceBaseCost;

/**
 * Получение расстояния для базовой стоимости
 */
public interface DistanceBaseCostProvider {
    /**
     * @return Расстояние для базовой стоимости
     */
    int getDistanceBaseCost();
}

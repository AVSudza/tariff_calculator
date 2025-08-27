package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.fastdelivery.domain.common.distanceBaseCost.DistanceBaseCostProvider;
import ru.fastdelivery.domain.common.stepNormalize.StepNormalizeProvider;

/**
 * Настройки шага нормализации из конфига
 */
@Configuration
@ConfigurationProperties("dimension")
@Setter
public class DimensionProperty implements StepNormalizeProvider, DistanceBaseCostProvider {
    private int stepNormalize;
    private int distanceBaseCost;

    @Override
    public int getStepNormalize() {
        return stepNormalize;
    }

    @Override
    public int getDistanceBaseCost() {
        return distanceBaseCost;
    }
}

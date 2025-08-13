package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.fastdelivery.domain.common.stepNormalize.StepNormalizeProvider;

/**
 * Настройки шага нормализации из конфига
 */
@Configuration
@ConfigurationProperties("dimension")
@Setter
public class StepNormalizeProperty implements StepNormalizeProvider {
    private int stepNormalize;

    @Override
    public int get() {
        return stepNormalize;
    }
}

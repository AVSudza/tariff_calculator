package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.fastdelivery.domain.common.height.HeightPropertiesProvider;

import java.math.BigInteger;

/**
 * Настройки высоты упаковки из конфига
 */
@Configuration
@ConfigurationProperties("dimension.height")
@Setter
public class HeightProperties implements HeightPropertiesProvider {
    private int max;
    private int min;

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getMin() {
        return min;
    }

}

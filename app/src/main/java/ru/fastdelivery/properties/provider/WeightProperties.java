package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.fastdelivery.domain.common.weight.WeightPropertiesProvider;

import java.math.BigInteger;

/**
 * Настройки веса из конфига
 */
@Configuration
@ConfigurationProperties("dimension.weight")
@Setter
public class WeightProperties implements WeightPropertiesProvider {
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

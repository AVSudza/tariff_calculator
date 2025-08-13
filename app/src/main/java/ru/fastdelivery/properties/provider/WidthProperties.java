package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.fastdelivery.domain.common.width.WidthPropertiesProvider;

import java.math.BigInteger;

/**
 * Настройки веса из конфига
 */
@Configuration
@ConfigurationProperties("dimension.width")
@Setter
public class WidthProperties implements WidthPropertiesProvider {
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

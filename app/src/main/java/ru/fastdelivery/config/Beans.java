package ru.fastdelivery.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.currency.CurrencyPropertiesProvider;
import ru.fastdelivery.usecase.CoordinatesPropertiesProvider;
import ru.fastdelivery.usecase.*;

/**
 * Определение реализаций бинов для всех модулей приложения
 */
@Configuration
public class Beans {

    @Bean
    public CurrencyFactory currencyFactory(CurrencyPropertiesProvider currencyProperties) {
        return new CurrencyFactory(currencyProperties);
    }

    @Bean
    public TariffCalculateUseCase tariffCalculateUseCase(WeightPriceProvider weightPriceProvider,
                                                         @Qualifier("coordinatesProperties") CoordinatesPropertiesProvider coordinatesPropertiesProvider) {
        return new TariffCalculateUseCase(weightPriceProvider,
                coordinatesPropertiesProvider);
    }
}

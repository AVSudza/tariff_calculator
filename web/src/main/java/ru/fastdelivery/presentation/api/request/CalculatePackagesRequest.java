package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.fastdelivery.domain.delivery.points.DeparturePoint;
import ru.fastdelivery.domain.delivery.points.DestinationPoint;

import java.util.List;

/**
 * Запрос на вычисление пакетов
 *
 * @param packages     Список упаковок отправления
 * @param currencyCode Трехбуквенный код валюты
 * @param destination Координаты пункта отправления
 * @param departure Координаты пункта получения
 */
@Schema(description = "Данные для расчета стоимости доставки")
public record CalculatePackagesRequest(
        @Schema(description = "Список упаковок отправления",
                example = "[{\"weight\": 4564, \"length\": 345, " +
                        "\"width\": 589, \"height\": 234}," +
                        "{\"weight\": 4056, \"length\": 456, " +
                        "\"width\": 405, \"height\": 645}]")
        @NotNull
        List<CargoPackage> packages,

        @Schema(description = "Трехбуквенный код валюты", example = "RUB")
        @NotNull
        String currencyCode,

        @Schema(description = "Координаты пункта отправления",
                example = "{\"latitude\": 73.398660, \"longitude\": 55.027532}")
        @NotNull
        DestinationPoint destination,

        @Schema(description = "Координаты пункта получения",
                example = "{\"latitude\": 77.398660, \"longitude\": 54.027532}")
        @NotNull
        DeparturePoint departure) {
}

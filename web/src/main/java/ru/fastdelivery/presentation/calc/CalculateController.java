package ru.fastdelivery.presentation.calc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.height.Height;
import ru.fastdelivery.domain.common.height.HeightPropertiesProvider;
import ru.fastdelivery.domain.common.length.Length;
import ru.fastdelivery.domain.common.length.LengthPropertiesProvider;
import ru.fastdelivery.domain.common.stepNormalize.StepNormalizeFactory;
import ru.fastdelivery.domain.common.stepNormalize.StepNormalizeProvider;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.common.weight.WeightPropertiesProvider;
import ru.fastdelivery.domain.common.width.Width;
import ru.fastdelivery.domain.common.width.WidthPropertiesProvider;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/calculate/")
@RequiredArgsConstructor
@Tag(name = "Расчеты стоимости доставки")
public class CalculateController {
    private final TariffCalculateUseCase tariffCalculateUseCase;
    private final CurrencyFactory currencyFactory;
    private final WeightPropertiesProvider weightPropertiesProvider;
    private final LengthPropertiesProvider lengthPropertiesProvider;
    private final WidthPropertiesProvider widthPropertiesProvider;
    private final HeightPropertiesProvider heightPropertiesProvider;
    private final StepNormalizeProvider stepNormalizeProvider;

    @PostMapping
    @Operation(summary = "Расчет стоимости по упаковкам груза")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    public CalculatePackagesResponse calculate(
            @Valid @RequestBody CalculatePackagesRequest request) {

        var packs = request.packages().stream()
                .map(cargoPackage -> new Pack(
                                new Weight(cargoPackage.weight(), weightPropertiesProvider.getMax()),
                                new Length(cargoPackage.length(), lengthPropertiesProvider.getMax()),
                                new Width(cargoPackage.width(), widthPropertiesProvider.getMax()),
                                new Height(cargoPackage.height(), heightPropertiesProvider.getMax()),
                                BigInteger.valueOf(new StepNormalizeFactory(stepNormalizeProvider).create())
                        )
                )
                .toList();

        var shipment = new Shipment(packs, currencyFactory.create(request.currencyCode()),
                request.destination(), request.departure());
        var calculatedPrice = tariffCalculateUseCase.calc(shipment);
        var minimalPrice = tariffCalculateUseCase.minimalPrice();
        return new CalculatePackagesResponse(calculatedPrice, minimalPrice);
    }
}


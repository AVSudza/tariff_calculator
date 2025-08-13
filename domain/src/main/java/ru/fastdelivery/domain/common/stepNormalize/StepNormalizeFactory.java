package ru.fastdelivery.domain.common.stepNormalize;

import lombok.RequiredArgsConstructor;

/**
 * Создание шага нормализации
 */
@RequiredArgsConstructor
public class StepNormalizeFactory {

    private final StepNormalizeProvider stepNormalizeProvider;

    public int create() {
        return stepNormalizeProvider.get();
    }
}

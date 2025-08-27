package ru.fastdelivery.usecase;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HaversinesWithAntipodes {
    private final int RADIUS_EARTH = 6_372_795;
    private final BigDecimal pointOneLat;
    private final BigDecimal pointOneLong;
    private final BigDecimal pointTwoLat;
    private final BigDecimal pointTwoLong;

    public HaversinesWithAntipodes(BigDecimal pointOneLat,
                                   BigDecimal pointOneLong,
                                   BigDecimal pointTwoLat,
                                   BigDecimal pointTwoLong) {
        this.pointOneLat = pointOneLat;
        this.pointOneLong = pointOneLong;
        this.pointTwoLat = pointTwoLat;
        this.pointTwoLong = pointTwoLong;
    }

    public BigDecimal getDistance() {
        BigDecimal pointOneLatOnRad = degreesToRad(pointOneLat);
        BigDecimal pointOneLongOnRad = degreesToRad(pointOneLong);
        BigDecimal pointTwoLatOnRad = degreesToRad(pointTwoLat);
        BigDecimal pointTwoLongOnRad = degreesToRad(pointTwoLong);
        BigDecimal cosPointOneLat = BigDecimal.valueOf(Math.cos(pointOneLatOnRad.doubleValue()));
        BigDecimal cosPointTwoLat = BigDecimal.valueOf(Math.cos(pointTwoLatOnRad.doubleValue()));
        BigDecimal sinPointOneLat = BigDecimal.valueOf(Math.sin(pointOneLatOnRad.doubleValue()));
        BigDecimal sinPointTwoLat = BigDecimal.valueOf(Math.sin(pointTwoLatOnRad.doubleValue()));
        BigDecimal deltaLong = pointTwoLongOnRad.subtract(pointOneLongOnRad);
        BigDecimal cosDelta = BigDecimal.valueOf(Math.cos(deltaLong.doubleValue()));
        BigDecimal sinDelta = BigDecimal.valueOf(Math.sin(deltaLong.doubleValue()));
        BigDecimal lengthLargeCircleY = BigDecimal.valueOf(Math.sqrt(
                Math.pow(cosPointTwoLat.multiply(sinDelta).doubleValue(), 2) +
                        Math.pow(cosPointOneLat
                                .multiply(sinPointTwoLat)
                                .subtract(sinPointOneLat
                                        .multiply(cosPointTwoLat)
                                        .multiply(cosDelta))
                                .doubleValue(), 2)
        ));
        BigDecimal lengthLargeCircleX = sinPointOneLat
                .multiply(sinPointTwoLat)
                .add(cosPointOneLat.multiply(cosPointTwoLat).multiply(cosDelta));
        return BigDecimal.valueOf(Math.atan2(lengthLargeCircleY.doubleValue(),
                lengthLargeCircleX.doubleValue()) * RADIUS_EARTH)
                .divide(BigDecimal.valueOf(1000), RoundingMode.HALF_UP)
                .setScale(0, RoundingMode.HALF_UP);
    }

    BigDecimal degreesToRad(BigDecimal degrees) {
        return degrees
                .multiply(BigDecimal.valueOf(Math.PI))
                .divide(BigDecimal.valueOf(180), 100, RoundingMode.HALF_UP);
    }
}

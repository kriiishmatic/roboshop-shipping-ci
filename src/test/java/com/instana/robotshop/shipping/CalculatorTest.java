
package com.instana.robotshop.shipping;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {

    @Test
    void calculatesZeroDistanceForSameCoordinates() {
        Calculator calculator = new Calculator(51.5074, -0.1278);

        assertEquals(0, calculator.getDistance(51.5074, -0.1278));
    }

    @Test
    void calculatesApproximateDistanceBetweenLondonAndParis() {
        Calculator calculator = new Calculator(51.5074, -0.1278);

        // London to Paris is approximately 344 km.
        assertEquals(344, calculator.getDistance(48.8566, 2.3522), 1);
    }

    @Test
    void distanceIsSymmetric() {
        Calculator london = new Calculator(51.5074, -0.1278);
        Calculator paris = new Calculator(48.8566, 2.3522);

        assertEquals(
            london.getDistance(48.8566, 2.3522),
            paris.getDistance(51.5074, -0.1278)
        );
    }

    @Test
    void canCalculateDistanceUsingCityCoordinates() {
        City city = new City();
        city.setLatitude(51.5074);
        city.setLongitude(-0.1278);

        Calculator calculator = new Calculator(city);

        assertEquals(344, calculator.getDistance(48.8566, 2.3522), 1);
    }

    // =====================================================
    // TEMPORARY FAILURE TEST - JENKINS FAILURE VALIDATION
    // =====================================================
    // Purpose:
    // Intentionally use an incorrect expected distance.
    //
    // Expected behaviour:
    // 1. Test compiles successfully.
    // 2. Test executes.
    // 3. Assertion fails.
    // 4. Maven returns BUILD FAILURE.
    // 5. Jenkins publishes the failed JUnit report.
    //
    // IMPORTANT:
    // Remove this test after validating Jenkins failure handling.

    @Test
    void shouldFailIntentionallyToValidateJenkinsPipeline() {
        Calculator calculator = new Calculator(51.5074, -0.1278);

        double actualDistance = calculator.getDistance(48.8566, 2.3522);

        // Intentionally incorrect expected value.
        assertEquals(999, actualDistance, 1,
                "Intentional failure for Jenkins CI validation");
    }
}

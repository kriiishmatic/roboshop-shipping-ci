package com.instana.robotshop.shipping;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipTest {

    @Test
    void defaultConstructorInitializesZeroValues() {
        Ship ship = new Ship();

        assertEquals(0, ship.getDistance());
        assertEquals(0.0, ship.getCost(), 0.0001);
    }

    @Test
    void parameterizedConstructorStoresDistanceAndCost() {
        Ship ship = new Ship(125, 6.25);

        assertEquals(125, ship.getDistance());
        assertEquals(6.25, ship.getCost(), 0.0001);
    }

    @Test
    void settersUpdateDistanceAndCost() {
        Ship ship = new Ship();
        ship.setDistance(250);
        ship.setCost(12.50);

        assertEquals(250, ship.getDistance());
        assertEquals(12.50, ship.getCost(), 0.0001);
    }

    @Test
    void toStringContainsDistanceAndCost() {
        Ship ship = new Ship(125, 6.25);

        assertTrue(ship.toString().contains("Distance: 125"));
        assertTrue(ship.toString().contains("Cost: 6.250000"));
    }
}

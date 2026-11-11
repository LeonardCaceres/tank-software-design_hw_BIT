package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankStateTest {

    private static final float SPEED = 0.4f;

    @Test
    void startsInLightState() {
        Tank tank = new Tank(new Position(0, 0), SPEED, 100, 25);
        assertEquals("Light", tank.getStateName());
        assertTrue(tank.getState().canShoot());
        assertEquals(1.0f, tank.getState().travelTimeMultiplier());
    }

    @Test
    void transitionsToMediumBelow70Percent() {
        Tank tank = new Tank(new Position(0, 0), SPEED, 100, 25);
        tank.takeDamage(30);
        assertEquals("Medium", tank.getStateName());
        assertEquals(2.0f, tank.getState().travelTimeMultiplier());
        assertTrue(tank.getState().canShoot());
    }

    @Test
    void transitionsToHeavyBelow15Percent() {
        Tank tank = new Tank(new Position(0, 0), SPEED, 100, 25);
        tank.takeDamage(86);
        assertEquals("Heavy", tank.getStateName());
        assertEquals(3.0f, tank.getState().travelTimeMultiplier());
        assertFalse(tank.getState().canShoot());
    }

    @Test
    void mediumTankTravelsTwiceSlower() {
        Level level = new Level(new Field(10, 8), new Position(0, 0));

        Tank lightTank = new Tank(new Position(0, 0), SPEED, 100, 25);
        Tank mediumTank = new Tank(new Position(0, 2), SPEED, 100, 25);
        mediumTank.takeDamage(50);

        level.addTank(lightTank);
        level.addTank(mediumTank);
        lightTank.move(Direction.RIGHT, level);
        mediumTank.move(Direction.RIGHT, level);

        lightTank.update(0.4f);
        mediumTank.update(0.4f);

        assertTrue(lightTank.isAtDestination());
        assertFalse(mediumTank.isAtDestination());

        mediumTank.update(0.4f);
        assertTrue(mediumTank.isAtDestination());
    }

    @Test
    void heavyTankCannotShoot() {
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        Tank heavy = new Tank(new Position(2, 2), SPEED, 100, 25);
        heavy.takeDamage(90);
        level.addTank(heavy);

        heavy.shoot(level);
        assertTrue(level.getBullets().isEmpty());
    }

    @Test
    void futureStatesCanOverride() {
        TankState boost = new TankState() {
            @Override public String getName()                { return "Boost"; }
            @Override public float  travelTimeMultiplier()   { return 0.5f; }
            @Override public float  shootCooldownMultiplier(){ return 0.5f; }
            @Override public boolean canShoot()              { return true; }
        };
        Tank tank = new Tank(new Position(0, 0), SPEED);
        tank.setState(boost);
        assertEquals("Boost", tank.getStateName());
        assertEquals(0.5f, tank.getState().travelTimeMultiplier());
    }
}
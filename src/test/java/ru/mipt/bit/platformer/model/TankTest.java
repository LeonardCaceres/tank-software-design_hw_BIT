package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {

    private static final float SPEED = 0.4f;

    @Test
    void startsAtGivenPositionAndAtDestination() {
        Tank tank = new Tank(new Position(2, 2), SPEED);
        assertEquals(new Position(2, 2), tank.getPosition());
        assertTrue(tank.isAtDestination());
    }

    @Test
    void moveChangesDirectionImmediately() {
        Tank tank = new Tank(new Position(2, 2), SPEED);
        tank.move(Direction.UP, new Field());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void moveIntoEmptyCellStartsMovement() {
        Tank tank = new Tank(new Position(2, 2), SPEED);
        tank.move(Direction.RIGHT, new Field());
        assertEquals(new Position(3, 2), tank.getDestinationPosition());
        assertFalse(tank.isAtDestination());
    }

    @Test
    void moveIntoObstacleDoesNotStartMovement() {
        Field field = new Field();
        field.addTree(new Tree(new Position(3, 2)));
        Tank tank = new Tank(new Position(2, 2), SPEED);
        tank.move(Direction.RIGHT, field);
        assertEquals(new Position(2, 2), tank.getDestinationPosition());
        assertTrue(tank.isAtDestination());
    }

    @Test
    void updateAdvancesProgressAndCompletesMovement() {
        Tank tank = new Tank(new Position(2, 2), SPEED);
        tank.move(Direction.RIGHT, new Field());
        tank.update(10f);
        assertTrue(tank.isAtDestination());
        assertEquals(new Position(3, 2), tank.getPosition());
    }

    @Test
    void moveIsIgnoredWhileMoving() {
        Tank tank = new Tank(new Position(2, 2), SPEED);
        tank.move(Direction.RIGHT, new Field());
        tank.move(Direction.UP, new Field());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void movementSpeedIsConfigurable() {
        Tank fast = new Tank(new Position(0, 0), 0.1f);
        Tank slow = new Tank(new Position(0, 0), 1.0f);
        fast.move(Direction.RIGHT, new Field());
        slow.move(Direction.RIGHT, new Field());
        fast.update(0.5f);
        slow.update(0.5f);
        assertTrue(fast.isAtDestination());
        assertFalse(slow.isAtDestination());
    }
}
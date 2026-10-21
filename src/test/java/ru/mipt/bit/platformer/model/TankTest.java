package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final float SPEED = 0.4f;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;

    private static Level emptyLevel() {
        return new Level(new Field(WIDTH, HEIGHT), new Position(0, 0));
    }

    @Test
    void startsAtGivenPositionAndAtDestination() {
        Tank tank = new Tank(new Position(2, 2), SPEED);
        assertEquals(new Position(2, 2), tank.getPosition());
        assertTrue(tank.isAtDestination());
    }

    @Test
    void moveChangesDirectionImmediately() {
        Tank tank = new Tank(new Position(2, 2), SPEED);
        Level level = emptyLevel();
        level.addTank(tank);
        tank.move(Direction.UP, level);
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void moveIntoEmptyCellStartsMovement() {
        Tank tank = new Tank(new Position(2, 2), SPEED);
        Level level = emptyLevel();
        level.addTank(tank);
        tank.move(Direction.RIGHT, level);
        assertEquals(new Position(3, 2), tank.getDestinationPosition());
        assertFalse(tank.isAtDestination());
    }

    @Test
    void moveIntoObstacleDoesNotStartMovement() {
        Field field = new Field(WIDTH, HEIGHT);
        field.addTree(new Tree(new Position(3, 2)));
        Level level = new Level(field, new Position(0, 0));
        Tank tank = new Tank(new Position(2, 2), SPEED);
        level.addTank(tank);
        tank.move(Direction.RIGHT, level);
        assertEquals(new Position(2, 2), tank.getDestinationPosition());
        assertTrue(tank.isAtDestination());
    }

    @Test
    void moveOutOfBoundsIsRejected() {
        Tank tank = new Tank(new Position(0, 0), SPEED);
        Level level = emptyLevel();
        level.addTank(tank);
        tank.move(Direction.LEFT, level);
        assertEquals(new Position(0, 0), tank.getDestinationPosition());
        assertTrue(tank.isAtDestination());
    }

    @Test
    void moveIntoAnotherTankPositionIsRejected() {
        Level level = emptyLevel();
        Tank t1 = new Tank(new Position(2, 2), SPEED);
        Tank t2 = new Tank(new Position(3, 2), SPEED);
        level.addTank(t1);
        level.addTank(t2);

        t1.move(Direction.RIGHT, level);
        assertEquals(new Position(2, 2), t1.getDestinationPosition());
        assertTrue(t1.isAtDestination());
    }

    @Test
    void moveIntoAnotherTankDestinationIsRejected() {
        Level level = emptyLevel();
        Tank t1 = new Tank(new Position(2, 2), SPEED);
        level.addTank(t1);
        t1.move(Direction.RIGHT, level);

        Tank t2 = new Tank(new Position(3, 3), SPEED);
        level.addTank(t2);
        t2.move(Direction.DOWN, level);

        assertEquals(new Position(3, 3), t2.getDestinationPosition());
        assertTrue(t2.isAtDestination());
    }

    @Test
    void updateAdvancesProgressAndCompletesMovement() {
        Tank tank = new Tank(new Position(2, 2), SPEED);
        Level level = emptyLevel();
        level.addTank(tank);
        tank.move(Direction.RIGHT, level);
        tank.update(10f);
        assertTrue(tank.isAtDestination());
        assertEquals(new Position(3, 2), tank.getPosition());
    }

    @Test
    void moveIsIgnoredWhileMoving() {
        Tank tank = new Tank(new Position(2, 2), SPEED);
        Level level = emptyLevel();
        level.addTank(tank);
        tank.move(Direction.RIGHT, level);
        tank.move(Direction.UP, level);
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void movementSpeedIsConfigurable() {
        Tank fast = new Tank(new Position(0, 0), 0.1f);
        Tank slow = new Tank(new Position(0, 2), 1.0f);
        Level level = emptyLevel();
        level.addTank(fast);
        level.addTank(slow);
        fast.move(Direction.RIGHT, level);
        slow.move(Direction.RIGHT, level);
        fast.update(0.5f);
        slow.update(0.5f);
        assertTrue(fast.isAtDestination());
        assertFalse(slow.isAtDestination());
    }
}
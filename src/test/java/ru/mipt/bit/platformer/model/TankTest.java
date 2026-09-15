package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {

    @Test
    void startsAtGivenPositionAndAtDestination() {
        Tank tank = new Tank(new GridPoint2(2, 2));
        assertEquals(new GridPoint2(2, 2), tank.getCoordinates());
        assertTrue(tank.isAtDestination());
    }

    @Test
    void moveChangesDirectionImmediately() {
        Tank tank = new Tank(new GridPoint2(2, 2));
        tank.move(Direction.UP, new Field());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void moveIntoEmptyCellStartsMovement() {
        Tank tank = new Tank(new GridPoint2(2, 2));
        tank.move(Direction.RIGHT, new Field());
        assertEquals(new GridPoint2(3, 2), tank.getDestinationCoordinates());
        assertFalse(tank.isAtDestination());
    }

    @Test
    void moveIntoObstacleDoesNotStartMovement() {
        Field field = new Field();
        field.addTree(new Tree(new GridPoint2(3, 2)));
        Tank tank = new Tank(new GridPoint2(2, 2));
        tank.move(Direction.RIGHT, field);
        assertEquals(new GridPoint2(2, 2), tank.getDestinationCoordinates());
        assertTrue(tank.isAtDestination());
    }

    @Test
    void updateAdvancesProgressAndCompletesMovement() {
        Tank tank = new Tank(new GridPoint2(2, 2));
        tank.move(Direction.RIGHT, new Field());
        tank.update(10f);
        assertTrue(tank.isAtDestination());
        assertEquals(new GridPoint2(3, 2), tank.getCoordinates());
    }

    @Test
    void moveIsIgnoredWhileMoving() {
        Tank tank = new Tank(new GridPoint2(2, 2));
        tank.move(Direction.RIGHT, new Field());
        tank.move(Direction.UP, new Field());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }
}
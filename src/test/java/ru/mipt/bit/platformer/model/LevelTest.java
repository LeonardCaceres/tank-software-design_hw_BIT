package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private static Level level(int w, int h) {
        return new Level(new Field(w, h), new Position(0, 0));
    }

    @Test
    void isWithinBounds() {
        Level level = level(10, 8);
        assertTrue(level.isWithinBounds(new Position(0, 0)));
        assertTrue(level.isWithinBounds(new Position(9, 7)));
        assertFalse(level.isWithinBounds(new Position(-1, 0)));
        assertFalse(level.isWithinBounds(new Position(10, 0)));
        assertFalse(level.isWithinBounds(new Position(0, -1)));
        assertFalse(level.isWithinBounds(new Position(0, 8)));
    }

    @Test
    void cannotMoveOutsideBounds() {
        Level level = level(10, 8);
        Tank tank = new Tank(new Position(0, 0), 0.4f);
        level.addTank(tank);
        assertFalse(level.canMove(tank, new Position(-1, 0)));
        assertFalse(level.canMove(tank, new Position(10, 0)));
    }

    @Test
    void cannotMoveIntoTree() {
        Field field = new Field(10, 8);
        field.addTree(new Tree(new Position(2, 2)));
        Level level = new Level(field, new Position(0, 0));
        Tank tank = new Tank(new Position(1, 2), 0.4f);
        level.addTank(tank);
        assertFalse(level.canMove(tank, new Position(2, 2)));
    }

    @Test
    void cannotMoveIntoOtherTankPosition() {
        Level level = level(10, 8);
        Tank t1 = new Tank(new Position(1, 1), 0.4f);
        Tank t2 = new Tank(new Position(2, 2), 0.4f);
        level.addTank(t1);
        level.addTank(t2);
        assertFalse(level.canMove(t1, new Position(2, 2)));
    }

    @Test
    void cannotMoveIntoOtherTankDestination() {
        Level level = level(10, 8);
        Tank t1 = new Tank(new Position(2, 2), 0.4f);
        level.addTank(t1);
        t1.move(Direction.RIGHT, level);
        Tank t2 = new Tank(new Position(3, 3), 0.4f);
        level.addTank(t2);
        assertFalse(level.canMove(t2, new Position(3, 2)));
    }

    @Test
    void canMoveIntoOwnCurrentCellIgnoredByExclusion() {
        Level level = level(10, 8);
        Tank tank = new Tank(new Position(2, 2), 0.4f);
        level.addTank(tank);
        assertTrue(level.canMove(tank, new Position(3, 2)));
    }
}
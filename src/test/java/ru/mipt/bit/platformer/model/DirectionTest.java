package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    private static final Position ORIGIN = new Position(0, 0);

    @Test
    void upIncrementsY() {
        assertEquals(new Position(0, 1), Direction.UP.next(ORIGIN));
    }

    @Test
    void downDecrementsY() {
        assertEquals(new Position(0, -1), Direction.DOWN.next(ORIGIN));
    }

    @Test
    void leftDecrementsX() {
        assertEquals(new Position(-1, 0), Direction.LEFT.next(ORIGIN));
    }

    @Test
    void rightIncrementsX() {
        assertEquals(new Position(1, 0), Direction.RIGHT.next(ORIGIN));
    }

    @Test
    void rotationsAreExpected() {
        assertEquals(90f,   Direction.UP.getRotation());

        assertEquals(-90f,  Direction.DOWN.getRotation());
        assertEquals(-180f, Direction.LEFT.getRotation());
        assertEquals(0f,    Direction.RIGHT.getRotation());
    }
}
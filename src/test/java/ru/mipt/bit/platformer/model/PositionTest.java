package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void keepsCoordinates() {
        Position p = new Position(3, 5);
        assertEquals(3, p.getX());
        assertEquals(5, p.getY());
    }

    @Test
    void translatedReturnsNewInstance() {
        Position p = new Position(1, 1);
        Position q = p.translated(2, -3);
        assertEquals(new Position(3, -2), q);
        assertEquals(new Position(1, 1), p);   // оригинал не изменился
    }

    @Test
    void equalsAndHashCode() {
        assertEquals(new Position(2, 2), new Position(2, 2));
        assertEquals(new Position(2, 2).hashCode(),
                new Position(2, 2).hashCode());
        assertNotEquals(new Position(2, 2), new Position(2, 3));
    }
}
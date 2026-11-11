package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldTest {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;

    @Test
    void emptyFieldHasNoObstacles() {
        Field field = new Field(WIDTH, HEIGHT);
        assertFalse(field.isObstacle(new Position(0, 0)));
        assertFalse(field.isObstacle(new Position(5, 5)));
    }

    @Test
    void treeMakesItsCellAnObstacle() {
        Field field = new Field(WIDTH, HEIGHT);
        field.addObstacle(new Tree(new Position(1, 3)));
        assertTrue(field.isObstacle(new Position(1, 3)));
        assertFalse(field.isObstacle(new Position(1, 4)));
    }

    @Test
    void getObstaclesIsUnmodifiable() {
        Field field = new Field(WIDTH, HEIGHT);
        field.addObstacle(new Tree(new Position(1, 3)));
        org.junit.jupiter.api.Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> field.getObstacles().add(new Tree(new Position(2, 2))));
    }

    @Test
    void dimensionsAreStored() {
        Field field = new Field(WIDTH, HEIGHT);
        assertEquals(WIDTH, field.getWidth());
        assertEquals(HEIGHT, field.getHeight());
    }
}
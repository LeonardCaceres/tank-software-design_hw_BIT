package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        field.addTree(new Tree(new Position(1, 3)));
        assertTrue(field.isObstacle(new Position(1, 3)));
        assertFalse(field.isObstacle(new Position(1, 4)));
    }

    @Test
    void getTreesIsUnmodifiable() {
        Field field = new Field(WIDTH, HEIGHT);
        field.addTree(new Tree(new Position(1, 3)));
        assertThrows(UnsupportedOperationException.class,
                () -> field.getTrees().add(new Tree(new Position(2, 2))));
    }

    @Test
    void dimensionsAreStored() {
        Field field = new Field(WIDTH, HEIGHT);
        org.junit.jupiter.api.Assertions.assertEquals(WIDTH, field.getWidth());
        org.junit.jupiter.api.Assertions.assertEquals(HEIGHT, field.getHeight());
    }
}
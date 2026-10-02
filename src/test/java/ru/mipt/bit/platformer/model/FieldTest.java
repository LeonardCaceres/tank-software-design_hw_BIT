package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void emptyFieldHasNoObstacles() {
        Field field = new Field();
        assertFalse(field.isObstacle(new Position(0, 0)));
        assertFalse(field.isObstacle(new Position(5, 5)));
    }

    @Test
    void treeMakesItsCellAnObstacle() {
        Field field = new Field();
        field.addTree(new Tree(new Position(1, 3)));
        assertTrue(field.isObstacle(new Position(1, 3)));
        assertFalse(field.isObstacle(new Position(1, 4)));
    }

    @Test
    void getTreesIsUnmodifiable() {
        Field field = new Field();
        field.addTree(new Tree(new Position(1, 3)));
        assertThrows(UnsupportedOperationException.class,
                () -> field.getTrees().add(new Tree(new Position(2, 2))));
    }
}
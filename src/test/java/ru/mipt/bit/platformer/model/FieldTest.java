package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void emptyFieldHasNoObstacles() {
        Field field = new Field();
        assertFalse(field.isObstacle(new GridPoint2(0, 0)));
        assertFalse(field.isObstacle(new GridPoint2(5, 5)));
    }

    @Test
    void treeMakesItsCellAnObstacle() {
        Field field = new Field();
        field.addTree(new Tree(new GridPoint2(1, 3)));
        assertTrue(field.isObstacle(new GridPoint2(1, 3)));
        assertFalse(field.isObstacle(new GridPoint2(1, 4)));
    }

    @Test
    void getTreesIsUnmodifiable() {
        Field field = new Field();
        field.addTree(new Tree(new GridPoint2(1, 3)));
        assertThrows(UnsupportedOperationException.class,
                () -> field.getTrees().add(new Tree(new GridPoint2(2, 2))));
    }
}
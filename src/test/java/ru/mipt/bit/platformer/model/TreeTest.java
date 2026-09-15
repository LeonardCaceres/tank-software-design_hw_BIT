package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreeTest {

    @Test
    void keepsCoordinates() {
        Tree tree = new Tree(new GridPoint2(3, 4));
        assertEquals(new GridPoint2(3, 4), tree.getCoordinates());
    }

    @Test
    void getCoordinatesReturnsCopy() {
        Tree tree = new Tree(new GridPoint2(3, 4));
        GridPoint2 copy = tree.getCoordinates();
        copy.x = 99;
        assertEquals(new GridPoint2(3, 4), tree.getCoordinates());
    }
}
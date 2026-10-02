package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreeTest {

    @Test
    void keepsPosition() {
        Tree tree = new Tree(new Position(3, 4));
        assertEquals(new Position(3, 4), tree.getPosition());
    }
}
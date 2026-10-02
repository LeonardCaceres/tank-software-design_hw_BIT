package ru.mipt.bit.platformer.model;

public class Tree {
    private final Position position;

    public Tree(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
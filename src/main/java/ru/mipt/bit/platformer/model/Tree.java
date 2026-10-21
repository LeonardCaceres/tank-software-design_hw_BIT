package ru.mipt.bit.platformer.model;

import java.util.Objects;

public class Tree {
    private final Position position;

    public Tree(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tree)) return false;
        Tree tree = (Tree) o;
        return Objects.equals(position, tree.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return "Tree" + position;
    }
}
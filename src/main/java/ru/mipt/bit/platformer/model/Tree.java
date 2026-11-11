package ru.mipt.bit.platformer.model;

import java.util.Objects;

/**
 * <b>Domain Entity</b>. Static obstacle on the field.
 * <p>Implements the {@link Obstacle} domain contract; adding new obstacle types
 * (walls, water, etc.) does not require changes in {@link Field} or {@link Tank}.</p>
 */
public class Tree implements Obstacle {
    private final Position position;

    public Tree(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean blocks(Position position) {
        return this.position.equals(position);
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
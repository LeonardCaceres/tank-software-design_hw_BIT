package ru.mipt.bit.platformer.model;

/**
 * <b>Domain Port/Contract</b>. Anything that can block a cell on the field.
 * <p>Adding new obstacle types (walls, water, rubble) does not require changes
 * in {@link Tank}, {@link Field}, or {@link Level} — just implement this interface.</p>
 */
public interface Obstacle {
    boolean blocks(Position position);
}
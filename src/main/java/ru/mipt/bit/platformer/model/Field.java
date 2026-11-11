package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <b>Domain Entity</b>. Grid with obstacles.
 * <p>Obstacles are stored via the {@link Obstacle} contract, so new obstacle
 * types can be added without touching this class or {@link Tank}.</p>
 * <p>Mutation of obstacles is package-private: only {@link Level} is allowed
 * to add obstacles, so observer notifications cannot be bypassed by external code.</p>
 */
public class Field {
    private final int width;
    private final int height;
    private final List<Obstacle> obstacles = new ArrayList<>();

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    public List<Obstacle> getObstacles() {
        return Collections.unmodifiableList(obstacles);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isObstacle(Position position) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.blocks(position)) {
                return true;
            }
        }
        return false;
    }
}
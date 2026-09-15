package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Tank {
    public static final float MOVEMENT_SPEED = 0.4f;
    private static final float EPSILON = 1e-4f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private Direction direction = Direction.RIGHT;
    private float movementProgress = 1f;

    public Tank(GridPoint2 start) {
        this.coordinates = new GridPoint2(start);
        this.destinationCoordinates = new GridPoint2(start);
    }

    public void move(Direction newDirection, Field field) {
        if (!isAtDestination()) return;
        this.direction = newDirection;
        GridPoint2 next = newDirection.next(coordinates);
        if (!field.isObstacle(next)) {
            destinationCoordinates.set(next);
            movementProgress = 0f;
        }
    }

    public void update(float deltaTime) {
        if (isAtDestination()) return;
        movementProgress = Math.min(1f, movementProgress + deltaTime / MOVEMENT_SPEED);
        if (movementProgress >= 1f - EPSILON) {
            movementProgress = 1f;
            coordinates.set(destinationCoordinates);
        }
    }

    public boolean isAtDestination() {
        return Math.abs(movementProgress - 1f) < EPSILON;
    }

    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public Direction getDirection() {
        return direction;
    }

    public float getMovementProgress() {
        return movementProgress;
    }
}
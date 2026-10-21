package ru.mipt.bit.platformer.model;

public class Tank {
    private static final float EPSILON = 1e-4f;

    private final float movementSpeed;
    private Position position;
    private Position destinationPosition;
    private Direction direction = Direction.RIGHT;
    private float movementProgress = 1f;

    public Tank(Position start, float movementSpeed) {
        this.position = start;
        this.destinationPosition = start;
        this.movementSpeed = movementSpeed;
    }

    public void move(Direction newDirection, Level level) {
        if (!isAtDestination()) {
            return;
        }
        this.direction = newDirection;
        Position next = newDirection.next(position);
        if (level.canMove(this, next)) {
            destinationPosition = next;
            movementProgress = 0f;
        }
    }

    public void update(float deltaTime) {
        if (isAtDestination()) {
            return;
        }
        movementProgress = Math.min(1f, movementProgress + deltaTime / movementSpeed);
        if (movementProgress >= 1f - EPSILON) {
            movementProgress = 1f;
            position = destinationPosition;
        }
    }

    public boolean isAtDestination() {
        return Math.abs(movementProgress - 1f) < EPSILON;
    }

    public Position getPosition()            { return position; }
    public Position getDestinationPosition() { return destinationPosition; }
    public Direction getDirection()          { return direction; }
    public float getMovementProgress()       { return movementProgress; }
}
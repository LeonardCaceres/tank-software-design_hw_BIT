package ru.mipt.bit.platformer.model;

public class Bullet {
    private static final float EPSILON = 1e-4f;

    private final Tank owner;
    private final Direction direction;
    private final float speed;
    private final int damage;

    private Position position;
    private Position destinationPosition;
    private float movementProgress = 0f;

    public Bullet(Tank owner, Position start, Direction direction, float speed, int damage) {
        this.owner = owner;
        this.position = start;
        this.destinationPosition = direction.next(start);
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
    }

    public void update(float deltaTime) {
        movementProgress = Math.min(1f, movementProgress + deltaTime / speed);
    }

    public boolean isAtDestination() {
        return movementProgress >= 1f - EPSILON;
    }

    public void advance() {
        position = destinationPosition;
        destinationPosition = direction.next(position);
        movementProgress = 0f;
    }

    public Tank getOwner()                  { return owner; }
    public Direction getDirection()         { return direction; }
    public Position getPosition()           { return position; }
    public Position getDestinationPosition(){ return destinationPosition; }
    public float getMovementProgress()      { return movementProgress; }
    public int getDamage()                  { return damage; }
}
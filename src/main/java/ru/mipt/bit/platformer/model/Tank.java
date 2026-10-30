package ru.mipt.bit.platformer.model;

public class Tank {
    private static final float EPSILON = 1e-4f;
    private static final float BULLET_SPEED = 0.1f;
    private static final float SHOOT_COOLDOWN = 0.5f;

    private final float movementSpeed;
    private final int maxHealth;
    private final int bulletDamage;

    private Position position;
    private Position destinationPosition;
    private Direction direction = Direction.RIGHT;
    private float movementProgress = 1f;
    private int health;
    private float shootCooldownLeft = 0f;

    public Tank(Position start, float movementSpeed) {
        this(start, movementSpeed, 100, 25);
    }

    public Tank(Position start, float movementSpeed, int maxHealth, int bulletDamage) {
        this.position = start;
        this.destinationPosition = start;
        this.movementSpeed = movementSpeed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.bulletDamage = bulletDamage;
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

    public void shoot(Level level) {
        if (shootCooldownLeft > 0f) {
            return;
        }
        Position spawn = direction.next(position);
        Bullet bullet = new Bullet(this, spawn, direction, BULLET_SPEED, bulletDamage);
        level.addBullet(bullet);
        shootCooldownLeft = SHOOT_COOLDOWN;
    }

    public void update(float deltaTime) {
        if (shootCooldownLeft > 0f) {
            shootCooldownLeft = Math.max(0f, shootCooldownLeft - deltaTime);
        }
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

    public void takeDamage(int amount) {
        health -= amount;
    }

    public boolean isDead() {
        return health < 0;
    }

    public Position getPosition()            { return position; }
    public Position getDestinationPosition() { return destinationPosition; }
    public Direction getDirection()          { return direction; }
    public float getMovementProgress()       { return movementProgress; }
    public int getHealth()                   { return health; }
    public int getMaxHealth()                { return maxHealth; }
    public int getBulletDamage()             { return bulletDamage; }
}
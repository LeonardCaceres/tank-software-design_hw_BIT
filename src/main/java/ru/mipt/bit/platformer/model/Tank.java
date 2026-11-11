package ru.mipt.bit.platformer.model;

/**
 * <b>Domain Entity</b>. Player or AI tank.
 * <p>Uses the <b>State pattern</b>: behaviour (movement speed, ability to shoot,
 * shoot cooldown) depends on current HP percentage. New states (e.g. boosters
 * obtained on the field) can be added by implementing {@link TankState} and
 * calling {@link #setState(TankState)}.</p>
 * <p>Once the tank has been removed from {@link Level} ({@link #markRemoved()}),
 * subsequent move/shoot/update are no-ops, preventing the controller from
 * driving a tank that no longer exists.</p>
 */
public class Tank {
    private static final float EPSILON = 1e-4f;
    private static final float BULLET_SPEED = 0.1f;
    private static final float BASE_SHOOT_COOLDOWN = 0.5f;

    private final float baseMovementSpeed;
    private final int maxHealth;
    private final int bulletDamage;

    private Position position;
    private Position destinationPosition;
    private Direction direction = Direction.RIGHT;
    private float movementProgress = 1f;
    private int health;
    private float shootCooldownLeft = 0f;
    private boolean removed = false;
    private TankState state;

    public Tank(Position start, float movementSpeed) {
        this(start, movementSpeed, 100, 25);
    }

    public Tank(Position start, float movementSpeed, int maxHealth, int bulletDamage) {
        this.position = start;
        this.destinationPosition = start;
        this.baseMovementSpeed = movementSpeed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.bulletDamage = bulletDamage;
        this.state = LightState.INSTANCE;
    }

    public void move(Direction newDirection, Level level) {
        if (removed) return;
        if (!isAtDestination()) return;
        this.direction = newDirection;
        Position next = newDirection.next(position);
        if (level.canMove(this, next)) {
            destinationPosition = next;
            movementProgress = 0f;
        }
    }

    public void shoot(Level level) {
        if (removed) return;
        if (!state.canShoot()) return;
        if (shootCooldownLeft > 0f) return;
        Position spawn = direction.next(position);
        Bullet bullet = new Bullet(this, spawn, direction, BULLET_SPEED, bulletDamage);
        level.addBullet(bullet);
        shootCooldownLeft = BASE_SHOOT_COOLDOWN * state.shootCooldownMultiplier();
    }

    public void update(float deltaTime) {
        if (removed) return;
        if (shootCooldownLeft > 0f) {
            shootCooldownLeft = Math.max(0f, shootCooldownLeft - deltaTime);
        }
        if (isAtDestination()) return;
        float effectiveTravelSpeed = baseMovementSpeed * state.travelTimeMultiplier();
        movementProgress = Math.min(1f, movementProgress + deltaTime / effectiveTravelSpeed);
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
        updateState();
    }

    private void updateState() {
        float ratio = (float) health / maxHealth;
        if (ratio > 0.7f) {
            state = LightState.INSTANCE;
        } else if (ratio >= 0.15f) {
            state = MediumState.INSTANCE;
        } else {
            state = HeavyState.INSTANCE;
        }
    }

    public void setState(TankState state) {
        this.state = state;
    }

    public void markRemoved() {
        this.removed = true;
    }

    public boolean isRemoved() {
        return removed;
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
    public TankState getState()              { return state; }
    public String getStateName()             { return state.getName(); }
}
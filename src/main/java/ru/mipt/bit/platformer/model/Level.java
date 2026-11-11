package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <b>Domain Entity</b> (aggregate root).
 * <p>Single entry-point for level mutations: {@link #addTree}, {@link #addTank},
 * {@link #removeTank}, {@link #addBullet}, {@link #removeBullet}. External code
 * cannot bypass notifications by mutating the {@link Field} directly —
 * {@link Field#addObstacle} is package-private.</p>
 * <p>Observable: notifies {@link LevelObserver} subscribers (driven port).</p>
 */
public class Level {
    private final Field field;
    private final Position tankStart;
    private final List<Tank> tanks = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<LevelObserver> observers = new ArrayList<>();

    public Level(Field field, Position tankStart) {
        this.field = field;
        this.tankStart = tankStart;
    }

    public void addObserver(LevelObserver observer) {
        observers.add(observer);
        for (Obstacle obstacle : field.getObstacles()) {
            observer.onObstacleAdded(obstacle);
        }
        for (Tank tank : tanks) {
            observer.onTankAdded(tank);
        }
        for (Bullet bullet : bullets) {
            observer.onBulletAdded(bullet);
        }
    }

    public void addTree(Tree tree) {
        field.addObstacle(tree);
        for (LevelObserver observer : observers) {
            observer.onObstacleAdded(tree);
        }
    }

    public void addTank(Tank tank) {
        tanks.add(tank);
        for (LevelObserver observer : observers) {
            observer.onTankAdded(tank);
        }
    }

    public void removeTank(Tank tank) {
        if (tanks.remove(tank)) {
            tank.markRemoved();
            for (LevelObserver observer : observers) {
                observer.onTankRemoved(tank);
            }
        }
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
        for (LevelObserver observer : observers) {
            observer.onBulletAdded(bullet);
        }
        if (handleBulletCollisionAt(bullet, bullet.getPosition())) {
            removeBullet(bullet);
        }
    }

    public void removeBullet(Bullet bullet) {
        if (bullets.remove(bullet)) {
            for (LevelObserver observer : observers) {
                observer.onBulletRemoved(bullet);
            }
        }
    }

    public void tick(float deltaTime) {
        for (Tank tank : new ArrayList<>(tanks)) {
            if (!tanks.contains(tank)) {
                continue;
            }
            tank.update(deltaTime);
        }
        for (Bullet bullet : new ArrayList<>(bullets)) {
            if (!bullets.contains(bullet)) {
                continue;
            }
            bullet.update(deltaTime);
            if (bullet.isAtDestination()) {
                Position arrivedAt = bullet.getDestinationPosition();
                if (handleBulletCollisionAt(bullet, arrivedAt)) {
                    removeBullet(bullet);
                } else {
                    bullet.advance();
                }
            }
        }
    }

    private boolean handleBulletCollisionAt(Bullet bullet, Position position) {
        if (!isWithinBounds(position)) {
            return true;
        }
        if (field.isObstacle(position)) {
            return true;
        }
        for (Tank tank : new ArrayList<>(tanks)) {
            if (tank == bullet.getOwner()) {
                continue;
            }
            if (tank.getPosition().equals(position)
                    || tank.getDestinationPosition().equals(position)) {
                tank.takeDamage(bullet.getDamage());
                if (tank.isDead()) {
                    removeTank(tank);
                }
                return true;
            }
        }
        for (Bullet other : new ArrayList<>(bullets)) {
            if (other == bullet) {
                continue;
            }
            if (other.getPosition().equals(position)
                    || other.getDestinationPosition().equals(position)) {
                removeBullet(other);
                return true;
            }
        }
        return false;
    }

    public boolean isWithinBounds(Position position) {
        return position.getX() >= 0 && position.getX() < field.getWidth()
                && position.getY() >= 0 && position.getY() < field.getHeight();
    }

    public boolean canMove(Tank tank, Position position) {
        if (!isWithinBounds(position)) {
            return false;
        }
        if (field.isObstacle(position)) {
            return false;
        }
        for (Tank other : tanks) {
            if (other == tank) {
                continue;
            }
            if (other.getPosition().equals(position)) {
                return false;
            }
            if (other.getDestinationPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    public Field getField() {
        return field;
    }

    public Position getTankStart() {
        return tankStart;
    }

    public List<Tank> getTanks() {
        return Collections.unmodifiableList(tanks);
    }

    public List<Bullet> getBullets() {
        return Collections.unmodifiableList(bullets);
    }
}
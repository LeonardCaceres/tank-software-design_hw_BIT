package ru.mipt.bit.platformer.model;
/**
 * <b>Driven Port</b> (outbound from domain).
 * <p>Adapters (e.g. {@code GameRenderer}) subscribe to receive notifications
 * about mutations of {@link Level}. Domain remains infrastructure-agnostic.</p>
 */
public interface LevelObserver {
    void onTankAdded(Tank tank);
    void onTankRemoved(Tank tank);
    void onBulletAdded(Bullet bullet);
    void onBulletRemoved(Bullet bullet);
    void onObstacleAdded(Obstacle obstacle);
    void onObstacleRemoved(Obstacle obstacle);
}
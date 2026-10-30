package ru.mipt.bit.platformer.model;

public interface LevelObserver {
    void onTankAdded(Tank tank);
    void onTankRemoved(Tank tank);
    void onBulletAdded(Bullet bullet);
    void onBulletRemoved(Bullet bullet);
    void onTreeAdded(Tree tree);
    void onTreeRemoved(Tree tree);
}
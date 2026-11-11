package ru.mipt.bit.platformer.model;

/**
 * <b>Domain State</b>. 15% ≤ HP ≤ 70%. Travels 2× slower (travel-time × 2), can shoot normally.
 */
public enum MediumState implements TankState {
    INSTANCE;

    @Override public String getName()                { return "Medium"; }
    @Override public float  travelTimeMultiplier()   { return 2.0f; }
    @Override public float  shootCooldownMultiplier(){ return 1.0f; }
    @Override public boolean canShoot()              { return true; }
}
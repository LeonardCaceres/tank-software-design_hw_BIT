package ru.mipt.bit.platformer.model;

/**
 * <b>Domain State</b>. HP &lt; 15%. Travels 3× slower, cannot shoot.
 */
public enum HeavyState implements TankState {
    INSTANCE;

    @Override public String getName()                { return "Heavy"; }
    @Override public float  travelTimeMultiplier()   { return 3.0f; }
    @Override public float  shootCooldownMultiplier(){ return Float.POSITIVE_INFINITY; }
    @Override public boolean canShoot()              { return false; }
}
package ru.mipt.bit.platformer.model;

/**
 * <b>Domain State</b>. HP &gt; 70%. Behaves normally: baseline speed, can shoot.
 */
public enum LightState implements TankState {
    INSTANCE;

    @Override public String getName()                { return "Light"; }
    @Override public float  travelTimeMultiplier()   { return 1.0f; }
    @Override public float  shootCooldownMultiplier(){ return 1.0f; }
    @Override public boolean canShoot()              { return true; }
}
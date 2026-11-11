package ru.mipt.bit.platformer.model;

/**
 * <b>Domain</b>. State pattern contract for tank behaviour.
 * <p>Behaviour (movement speed, ability to shoot, shoot rate) is expressed by
 * properties of the state. New states (e.g. "boosted by pickup") can be added
 * by implementing this interface and calling {@link Tank#setState(TankState)}.</p>
 */
public interface TankState {

    String getName();

    float travelTimeMultiplier();

    float shootCooldownMultiplier();

    boolean canShoot();
}
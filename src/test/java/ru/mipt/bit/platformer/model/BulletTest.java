package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulletTest {

    private static Bullet newBullet(Tank owner, Position start, Direction dir) {
        return new Bullet(owner, start, dir, 0.1f, 25);
    }

    @Test
    void startsAtGivenPosition() {
        Tank owner = new Tank(new Position(0, 0), 0.4f);
        Bullet bullet = newBullet(owner, new Position(2, 2), Direction.RIGHT);
        assertEquals(new Position(2, 2), bullet.getPosition());
    }

    @Test
    void destinationIsNextCellInDirection() {
        Tank owner = new Tank(new Position(0, 0), 0.4f);
        Bullet bullet = newBullet(owner, new Position(2, 2), Direction.UP);
        assertEquals(new Position(2, 3), bullet.getDestinationPosition());
    }

    @Test
    void updateAdvancesProgress() {
        Tank owner = new Tank(new Position(0, 0), 0.4f);
        Bullet bullet = newBullet(owner, new Position(2, 2), Direction.RIGHT);
        bullet.update(0.05f);
        assertTrue(bullet.getMovementProgress() > 0f);
        assertFalse(bullet.isAtDestination());
    }

    @Test
    void isAtDestinationAfterFullProgress() {
        Tank owner = new Tank(new Position(0, 0), 0.4f);
        Bullet bullet = newBullet(owner, new Position(2, 2), Direction.RIGHT);
        bullet.update(1f);
        assertTrue(bullet.isAtDestination());
    }

    @Test
    void advanceMovesToNextCell() {
        Tank owner = new Tank(new Position(0, 0), 0.4f);
        Bullet bullet = newBullet(owner, new Position(2, 2), Direction.RIGHT);
        bullet.update(1f);
        bullet.advance();
        assertEquals(new Position(3, 2), bullet.getPosition());
        assertEquals(new Position(4, 2), bullet.getDestinationPosition());
    }

    @Test
    void damageIsStored() {
        Tank owner = new Tank(new Position(0, 0), 0.4f);
        Bullet bullet = new Bullet(owner, new Position(2, 2), Direction.RIGHT, 0.1f, 42);
        assertEquals(42, bullet.getDamage());
    }

    @Test
    void ownerIsStored() {
        Tank owner = new Tank(new Position(0, 0), 0.4f);
        Bullet bullet = newBullet(owner, new Position(2, 2), Direction.RIGHT);
        assertEquals(owner, bullet.getOwner());
    }
}
package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private static final float SPEED = 0.4f;

    private static Level level(int w, int h) {
        return new Level(new Field(w, h), new Position(0, 0));
    }

    @Test
    void isWithinBounds() {
        Level level = level(10, 8);
        assertTrue(level.isWithinBounds(new Position(0, 0)));
        assertTrue(level.isWithinBounds(new Position(9, 7)));
        assertFalse(level.isWithinBounds(new Position(-1, 0)));
        assertFalse(level.isWithinBounds(new Position(10, 0)));
        assertFalse(level.isWithinBounds(new Position(0, -1)));
        assertFalse(level.isWithinBounds(new Position(0, 8)));
    }

    @Test
    void cannotMoveOutsideBounds() {
        Level level = level(10, 8);
        Tank tank = new Tank(new Position(0, 0), SPEED);
        level.addTank(tank);
        assertFalse(level.canMove(tank, new Position(-1, 0)));
        assertFalse(level.canMove(tank, new Position(10, 0)));
    }

    @Test
    void cannotMoveIntoTree() {
        Field field = new Field(10, 8);
        field.addTree(new Tree(new Position(2, 2)));
        Level level = new Level(field, new Position(0, 0));
        Tank tank = new Tank(new Position(1, 2), SPEED);
        level.addTank(tank);
        assertFalse(level.canMove(tank, new Position(2, 2)));
    }

    @Test
    void cannotMoveIntoOtherTankPosition() {
        Level level = level(10, 8);
        Tank t1 = new Tank(new Position(1, 1), SPEED);
        Tank t2 = new Tank(new Position(2, 2), SPEED);
        level.addTank(t1);
        level.addTank(t2);
        assertFalse(level.canMove(t1, new Position(2, 2)));
    }

    @Test
    void cannotMoveIntoOtherTankDestination() {
        Level level = level(10, 8);
        Tank t1 = new Tank(new Position(2, 2), SPEED);
        level.addTank(t1);
        t1.move(Direction.RIGHT, level);
        Tank t2 = new Tank(new Position(3, 3), SPEED);
        level.addTank(t2);
        assertFalse(level.canMove(t2, new Position(3, 2)));
    }

    @Test
    void canMoveIntoOwnCurrentCellIgnoredByExclusion() {
        Level level = level(10, 8);
        Tank tank = new Tank(new Position(2, 2), SPEED);
        level.addTank(tank);
        assertTrue(level.canMove(tank, new Position(3, 2)));
    }

    @Test
    void bulletHitsTankAndDealsDamage() {
        Level level = level(10, 8);
        Tank shooter = new Tank(new Position(1, 1), SPEED);
        Tank target = new Tank(new Position(3, 1), SPEED, 100, 25);
        level.addTank(shooter);
        level.addTank(target);

        shooter.move(Direction.RIGHT, level);
        shooter.update(10f);
        shooter.shoot(level);

        for (int i = 0; i < 50; i++) {
            level.tick(0.1f);
        }

        assertEquals(75, target.getHealth());
        assertEquals(0, level.getBullets().size());
    }

    @Test
    void bulletDisappearsWhenHitsTree() {
        Field field = new Field(10, 8);
        field.addTree(new Tree(new Position(3, 1)));
        Level level = new Level(field, new Position(0, 0));
        Tank shooter = new Tank(new Position(1, 1), SPEED);
        level.addTank(shooter);

        shooter.move(Direction.RIGHT, level);
        shooter.update(10f);
        shooter.shoot(level);

        for (int i = 0; i < 50; i++) {
            level.tick(0.1f);
        }

        assertEquals(0, level.getBullets().size());
    }

    @Test
    void bulletDisappearsWhenLeavesBounds() {
        Level level = level(10, 8);
        Tank shooter = new Tank(new Position(9, 1), SPEED);
        level.addTank(shooter);

        shooter.move(Direction.RIGHT, level);
        shooter.update(10f);
        shooter.shoot(level);

        for (int i = 0; i < 50; i++) {
            level.tick(0.1f);
        }

        assertEquals(0, level.getBullets().size());
    }

    @Test
    void bulletDisappearsWhenHitsOtherBullet() {
        Level level = level(10, 8);
        Tank t1 = new Tank(new Position(1, 1), SPEED);
        Tank t2 = new Tank(new Position(9, 1), SPEED);
        level.addTank(t1);
        level.addTank(t2);

        t1.move(Direction.RIGHT, level);
        t1.update(10f);
        t2.move(Direction.LEFT, level);
        t2.update(10f);

        t1.shoot(level);
        t2.shoot(level);

        for (int i = 0; i < 100; i++) {
            level.tick(0.1f);
        }

        assertEquals(0, level.getBullets().size());
        assertEquals(100, t1.getHealth());
        assertEquals(100, t2.getHealth());
    }

    @Test
    void tankRemovedWhenKilled() {
        Level level = level(10, 8);
        Tank shooter = new Tank(new Position(1, 1), SPEED, 100, 200);
        Tank target = new Tank(new Position(3, 1), SPEED, 100, 25);
        level.addTank(shooter);
        level.addTank(target);

        shooter.move(Direction.RIGHT, level);
        shooter.update(10f);
        shooter.shoot(level);

        for (int i = 0; i < 50; i++) {
            level.tick(0.1f);
        }

        assertEquals(1, level.getTanks().size());
        assertEquals(shooter, level.getTanks().get(0));
    }

    @Test
    void bulletSpawnedOnAdjacentTankHitsImmediately() {
        Level level = level(10, 8);
        Tank shooter = new Tank(new Position(1, 1), SPEED);
        Tank target = new Tank(new Position(2, 1), SPEED);
        level.addTank(shooter);
        level.addTank(target);

        shooter.move(Direction.RIGHT, level);
        shooter.update(10f);
        shooter.shoot(level);

        assertEquals(0, level.getBullets().size());
        assertEquals(75, target.getHealth());
    }

    @Test
    void observersNotifiedOnTankAddRemove() {
        Level level = level(10, 8);
        RecordingObserver observer = new RecordingObserver();
        level.addObserver(observer);

        Tank tank = new Tank(new Position(1, 1), SPEED);
        level.addTank(tank);
        level.removeTank(tank);

        assertEquals(1, observer.tanksAdded.size());
        assertEquals(tank, observer.tanksAdded.get(0));
        assertEquals(1, observer.tanksRemoved.size());
        assertEquals(tank, observer.tanksRemoved.get(0));
    }

    @Test
    void observersNotifiedOnBulletAddRemove() {
        Level level = level(10, 8);
        RecordingObserver observer = new RecordingObserver();
        level.addObserver(observer);

        Tank tank = new Tank(new Position(1, 1), SPEED);
        level.addTank(tank);
        observer.reset();

        tank.shoot(level);
        Bullet bullet = level.getBullets().get(0);
        level.removeBullet(bullet);

        assertEquals(1, observer.bulletsAdded.size());
        assertEquals(1, observer.bulletsRemoved.size());
    }

    @Test
    void addObserverReplaysExistingObjects() {
        Level level = level(10, 8);
        Tank tank = new Tank(new Position(1, 1), SPEED);
        level.addTank(tank);

        RecordingObserver observer = new RecordingObserver();
        level.addObserver(observer);

        assertEquals(1, observer.tanksAdded.size());
        assertEquals(tank, observer.tanksAdded.get(0));
    }

    private static final class RecordingObserver implements LevelObserver {
        final List<Tank> tanksAdded = new ArrayList<>();
        final List<Tank> tanksRemoved = new ArrayList<>();
        final List<Bullet> bulletsAdded = new ArrayList<>();
        final List<Bullet> bulletsRemoved = new ArrayList<>();
        final List<Tree> treesAdded = new ArrayList<>();
        final List<Tree> treesRemoved = new ArrayList<>();

        void reset() {
            tanksAdded.clear();
            tanksRemoved.clear();
            bulletsAdded.clear();
            bulletsRemoved.clear();
            treesAdded.clear();
            treesRemoved.clear();
        }

        @Override public void onTankAdded(Tank tank)       { tanksAdded.add(tank); }
        @Override public void onTankRemoved(Tank tank)     { tanksRemoved.add(tank); }
        @Override public void onBulletAdded(Bullet bullet) { bulletsAdded.add(bullet); }
        @Override public void onBulletRemoved(Bullet bullet){ bulletsRemoved.add(bullet); }
        @Override public void onTreeAdded(Tree tree)       { treesAdded.add(tree); }
        @Override public void onTreeRemoved(Tree tree)     { treesRemoved.add(tree); }
    }
}
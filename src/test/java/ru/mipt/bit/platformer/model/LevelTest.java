package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

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
    void cannotMoveIntoTree() {
        Level level = level(10, 8);
        level.addTree(new Tree(new Position(2, 2)));
        Tank tank = new Tank(new Position(1, 2), 0.4f);
        level.addTank(tank);
        assertFalse(level.canMove(tank, new Position(2, 2)));
    }

    @Test
    void twoTanksCompetingForSameCellInOneStep_onlyOneSucceeds() {
        Level level = level(10, 8);
        Tank a = new Tank(new Position(1, 1), 0.4f);
        Tank b = new Tank(new Position(3, 1), 0.4f);
        level.addTank(a);
        level.addTank(b);

        a.move(Direction.RIGHT, level);
        b.move(Direction.LEFT, level);

        assertEquals(new Position(2, 1), a.getDestinationPosition());
        assertEquals(new Position(3, 1), b.getDestinationPosition());
        assertTrue(b.isAtDestination());
    }

    @Test
    void removedTankIsMarkedAndNoLongerNotified() {
        Level level = level(10, 8);
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        level.addTank(tank);

        List<String> events = new ArrayList<>();
        level.addObserver(new LevelObserver() {
            @Override public void onTankAdded(Tank t)    { events.add("add:" + t); }
            @Override public void onTankRemoved(Tank t)  { events.add("remove:" + t); }
            @Override public void onBulletAdded(Bullet b){ }
            @Override public void onBulletRemoved(Bullet b){ }
            @Override public void onObstacleAdded(Obstacle o){ }
            @Override public void onObstacleRemoved(Obstacle o){ }
        });

        level.removeTank(tank);

        assertTrue(tank.isRemoved());
        assertTrue(events.contains("remove:" + tank));
        assertTrue(level.getTanks().isEmpty());
    }

    @Test
    void addTreeNotifiesObserver_andCannotBeBypassed() {
        Level level = level(10, 8);
        List<Obstacle> added = new ArrayList<>();
        level.addObserver(new LevelObserver() {
            @Override public void onTankAdded(Tank t){ }
            @Override public void onTankRemoved(Tank t){ }
            @Override public void onBulletAdded(Bullet b){ }
            @Override public void onBulletRemoved(Bullet b){ }
            @Override public void onObstacleAdded(Obstacle o){ added.add(o); }
            @Override public void onObstacleRemoved(Obstacle o){ }
        });

        Tree tree = new Tree(new Position(3, 3));
        level.addTree(tree);

        assertEquals(1, added.size());
        assertTrue(level.getField().isObstacle(new Position(3, 3)));
    }
}
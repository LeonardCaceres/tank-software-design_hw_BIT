package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiTankControlStrategyTest {

    private static Level emptyLevel() {
        return new Level(new Field(10, 8), new Position(0, 0));
    }

    @Test
    void returnsCommandWhenTankAtDestination() {
        AiTankControlStrategy s = new AiTankControlStrategy(new Random(42));
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = emptyLevel();
        level.addTank(tank);

        assertTrue(s.nextCommand(tank, level).isPresent());
    }

    @Test
    void returnsEmptyWhenTankMoving() {
        AiTankControlStrategy s = new AiTankControlStrategy(new Random(42));
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = emptyLevel();
        level.addTank(tank);
        tank.move(Direction.RIGHT, level);

        assertTrue(s.nextCommand(tank, level).isEmpty());
    }

    @Test
    void sameSeedProducesSameCommand() {
        Tank t1 = new Tank(new Position(1, 1), 0.4f);
        Level l1 = emptyLevel();
        l1.addTank(t1);
        new AiTankControlStrategy(new Random(7)).nextCommand(t1, l1)
                .ifPresent(c -> c.execute());

        Tank t2 = new Tank(new Position(1, 1), 0.4f);
        Level l2 = emptyLevel();
        l2.addTank(t2);
        new AiTankControlStrategy(new Random(7)).nextCommand(t2, l2)
                .ifPresent(c -> c.execute());

        org.junit.jupiter.api.Assertions.assertEquals(t1.getDirection(), t2.getDirection());
        org.junit.jupiter.api.Assertions.assertEquals(
                l1.getBullets().size(), l2.getBullets().size());
    }

    @Test
    void eventuallyProducesShootCommand() {
        boolean sawShoot = false;
        Random random = new Random(123);
        Level level = emptyLevel();
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        level.addTank(tank);
        AiTankControlStrategy s = new AiTankControlStrategy(random);

        for (int i = 0; i < 100 && !sawShoot; i++) {

            tank.update(10f);
            tank.shoot(level);
            while (!level.getBullets().isEmpty()) {
                level.removeBullet(level.getBullets().get(0));
            }
            tank.shoot(level);
            while (!level.getBullets().isEmpty()) {
                level.removeBullet(level.getBullets().get(0));
            }
            tank.update(10f);

            int before = level.getBullets().size();
            s.nextCommand(tank, level).ifPresent(c -> c.execute());
            if (level.getBullets().size() > before) {
                sawShoot = true;
            }

            while (!level.getBullets().isEmpty()) {
                level.removeBullet(level.getBullets().get(0));
            }
            tank.update(10f);
            if (!tank.isAtDestination()) {
                tank.update(10f);
            }
        }
        assertTrue(sawShoot, "AI should eventually shoot with fixed seed");
    }
}
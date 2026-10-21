package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void sameSeedProducesSameDirection() {
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

        assertEquals(t1.getDirection(), t2.getDirection());
    }
}
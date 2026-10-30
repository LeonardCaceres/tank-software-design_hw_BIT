package ru.mipt.bit.platformer.command;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShootCommandTest {

    @Test
    void executeMakesTankShoot() {
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        Tank tank = new Tank(new Position(2, 2), 0.4f);
        level.addTank(tank);
        tank.move(Direction.RIGHT, level);
        tank.update(10f);

        new ShootCommand(tank, level).execute();

        assertEquals(1, level.getBullets().size());
        assertEquals(new Position(4, 2), level.getBullets().get(0).getPosition());
    }
}
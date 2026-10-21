package ru.mipt.bit.platformer.command;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveTankCommandTest {

    @Test
    void executeMovesTankInDirection() {
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        level.addTank(tank);

        new MoveTankCommand(tank, Direction.UP, level).execute();

        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new Position(1, 2), tank.getDestinationPosition());
    }

    @Test
    void executeRespectsObstacles() {
        Field field = new Field(10, 8);
        field.addTree(new Tree(new Position(1, 2)));
        Level level = new Level(field, new Position(0, 0));
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        level.addTank(tank);

        new MoveTankCommand(tank, Direction.UP, level).execute();

        assertEquals(Direction.UP, tank.getDirection());
        assertTrue(tank.isAtDestination());
        assertEquals(new Position(1, 1), tank.getDestinationPosition());
    }
}
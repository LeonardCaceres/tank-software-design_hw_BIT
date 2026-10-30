package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankControllerTest {

    @Test
    void createCommandDoesNotExecuteImmediately() {
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        level.addTank(tank);

        AtomicBoolean executed = new AtomicBoolean(false);
        TankControlStrategy strategy = (t, l) -> Optional.of(() -> {
            executed.set(true);
            t.move(Direction.UP, l);
        });

        TankController controller = new TankController(tank, strategy);
        controller.createCommand(level);

        assertEquals(false, executed.get());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void executeCommandExecutesPreviouslyCreated() {
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        level.addTank(tank);

        TankControlStrategy strategy = (t, l) -> Optional.of(() -> t.move(Direction.UP, l));

        TankController controller = new TankController(tank, strategy);
        controller.createCommand(level);
        controller.executeCommand();

        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void updateCombinesBothSteps() {
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        level.addTank(tank);

        TankControlStrategy strategy = (t, l) -> Optional.of(() -> t.move(Direction.UP, l));
        new TankController(tank, strategy).update(level);

        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void updateDoesNothingIfStrategyReturnsEmpty() {
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        level.addTank(tank);

        TankControlStrategy strategy = (t, l) -> Optional.empty();
        new TankController(tank, strategy).update(level);

        assertEquals(Direction.RIGHT, tank.getDirection());
        assertTrue(tank.isAtDestination());
    }
}
package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.input.InputSource;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTankControlStrategyTest {

    private static InputSource fakeInput(Integer... pressed) {
        Set<Integer> set = Set.of(pressed);
        return set::contains;
    }

    @Test
    void returnsEmptyWhenNoKeyPressed() {
        PlayerTankControlStrategy s = new PlayerTankControlStrategy(
                fakeInput(), Map.of(UP, Direction.UP), SPACE);
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        level.addTank(tank);

        assertTrue(s.nextCommand(tank, level).isEmpty());
    }

    @Test
    void returnsMoveCommandWhenDirectionKeyPressed() {
        PlayerTankControlStrategy s = new PlayerTankControlStrategy(
                fakeInput(UP), Map.of(UP, Direction.UP), SPACE);
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        level.addTank(tank);

        Optional<Command> cmd = s.nextCommand(tank, level);
        assertTrue(cmd.isPresent());
        cmd.get().execute();
        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new Position(1, 2), tank.getDestinationPosition());
    }

    @Test
    void returnsShootCommandWhenSpacePressed() {
        PlayerTankControlStrategy s = new PlayerTankControlStrategy(
                fakeInput(SPACE), Map.of(UP, Direction.UP), SPACE);
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        level.addTank(tank);

        s.nextCommand(tank, level).ifPresent(Command::execute);
        assertEquals(1, level.getBullets().size());
    }

    @Test
    void spaceTakesPriorityOverMovementKeys() {
        PlayerTankControlStrategy s = new PlayerTankControlStrategy(
                fakeInput(SPACE, UP), Map.of(UP, Direction.UP), SPACE);
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        level.addTank(tank);

        s.nextCommand(tank, level).ifPresent(Command::execute);
        assertEquals(1, level.getBullets().size());
        assertTrue(tank.isAtDestination());
    }

    @Test
    void differentKeysMapToDifferentDirections() {
        PlayerTankControlStrategy s = new PlayerTankControlStrategy(
                fakeInput(W), Map.of(W, Direction.UP), SPACE);
        Tank tank = new Tank(new Position(1, 1), 0.4f);
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        level.addTank(tank);

        s.nextCommand(tank, level).ifPresent(Command::execute);
        assertEquals(Direction.UP, tank.getDirection());
    }
}
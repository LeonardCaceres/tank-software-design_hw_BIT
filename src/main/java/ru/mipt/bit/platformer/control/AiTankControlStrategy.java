package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveTankCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Optional;
import java.util.Random;

public class AiTankControlStrategy implements TankControlStrategy {
    private final Random random;

    public AiTankControlStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Optional<Command> nextCommand(Tank tank, Level level) {
        if (!tank.isAtDestination()) {
            return Optional.empty();
        }
        Direction[] directions = Direction.values();
        Direction direction = directions[random.nextInt(directions.length)];
        return Optional.of(new MoveTankCommand(tank, direction, level));
    }
}
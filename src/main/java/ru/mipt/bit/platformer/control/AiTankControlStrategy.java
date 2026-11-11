package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveTankCommand;
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Optional;
import java.util.Random;

/**
 * <b>Application Service</b>. Simple AI: chooses between shooting and moving in a random direction.
 * <p>Skips commands for removed tanks and heavy tanks that cannot shoot.</p>
 */
public class AiTankControlStrategy implements TankControlStrategy {
    private static final int SHOOT_PROBABILITY_DENOMINATOR = 3;

    private final Random random;

    public AiTankControlStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Optional<Command> nextCommand(Tank tank, Level level) {
        if (tank.isRemoved()) {
            return Optional.empty();
        }
        if (!tank.isAtDestination()) {
            return Optional.empty();
        }

        if (tank.getState().canShoot()
                && random.nextInt(SHOOT_PROBABILITY_DENOMINATOR) == 0) {
            return Optional.of(new ShootCommand(tank, level));
        }

        Direction[] directions = Direction.values();
        Direction direction = directions[random.nextInt(directions.length)];
        return Optional.of(new MoveTankCommand(tank, direction, level));
    }
}
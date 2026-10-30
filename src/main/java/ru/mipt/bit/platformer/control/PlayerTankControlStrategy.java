package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveTankCommand;
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.input.InputSource;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Map;
import java.util.Optional;

public class PlayerTankControlStrategy implements TankControlStrategy {
    private final InputSource input;
    private final Map<Integer, Direction> keyBindings;
    private final int shootKey;

    public PlayerTankControlStrategy(InputSource input,
                                     Map<Integer, Direction> keyBindings,
                                     int shootKey) {
        this.input = input;
        this.keyBindings = keyBindings;
        this.shootKey = shootKey;
    }

    @Override
    public Optional<Command> nextCommand(Tank tank, Level level) {
        if (input.isKeyPressed(shootKey)) {
            return Optional.of(new ShootCommand(tank, level));
        }
        for (Map.Entry<Integer, Direction> entry : keyBindings.entrySet()) {
            if (input.isKeyPressed(entry.getKey())) {
                return Optional.of(new MoveTankCommand(tank, entry.getValue(), level));
            }
        }
        return Optional.empty();
    }
}
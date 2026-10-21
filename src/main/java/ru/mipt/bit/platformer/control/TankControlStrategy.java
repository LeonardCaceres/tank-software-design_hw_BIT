package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Optional;

public interface TankControlStrategy {
    Optional<Command> nextCommand(Tank tank, Level level);
}
package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

public class MoveTankCommand implements Command {
    private final Tank tank;
    private final Direction direction;
    private final Level level;

    public MoveTankCommand(Tank tank, Direction direction, Level level) {
        this.tank = tank;
        this.direction = direction;
        this.level = level;
    }

    @Override
    public void execute() {
        tank.move(direction, level);
    }
}
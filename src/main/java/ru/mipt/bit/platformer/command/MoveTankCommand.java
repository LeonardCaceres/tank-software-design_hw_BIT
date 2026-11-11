package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
/**
 * <b>Application</b>. Command that moves a tank in a given direction.
 * <p>Checks tank.isRemoved() defensively — if a tank died between command creation
 * and execution, the command becomes a no-op.</p>
 */
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
        if (tank.isRemoved()) return;
        tank.move(direction, level);
    }
}
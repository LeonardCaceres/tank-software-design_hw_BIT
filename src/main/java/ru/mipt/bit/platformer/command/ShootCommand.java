package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

/**
 * <b>Application</b>. Command that makes a tank shoot.
 * <p>Guards against acting on a removed tank.</p>
 */
public class ShootCommand implements Command {
    private final Tank tank;
    private final Level level;

    public ShootCommand(Tank tank, Level level) {
        this.tank = tank;
        this.level = level;
    }

    @Override
    public void execute() {
        if (tank.isRemoved()) return;
        tank.shoot(level);
    }
}
package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

public class ShootCommand implements Command {
    private final Tank tank;
    private final Level level;

    public ShootCommand(Tank tank, Level level) {
        this.tank = tank;
        this.level = level;
    }

    @Override
    public void execute() {
        tank.shoot(level);
    }
}
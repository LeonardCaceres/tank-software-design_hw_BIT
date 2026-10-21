package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

public class TankController {
    private final Tank tank;
    private final TankControlStrategy strategy;

    public TankController(Tank tank, TankControlStrategy strategy) {
        this.tank = tank;
        this.strategy = strategy;
    }

    public void update(Level level) {
        strategy.nextCommand(tank, level).ifPresent(Command::execute);
    }
}
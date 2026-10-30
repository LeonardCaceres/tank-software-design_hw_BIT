package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

public class TankController {
    private final Tank tank;
    private final TankControlStrategy strategy;
    private Command pendingCommand;

    public TankController(Tank tank, TankControlStrategy strategy) {
        this.tank = tank;
        this.strategy = strategy;
    }

    public void createCommand(Level level) {
        pendingCommand = strategy.nextCommand(tank, level).orElse(null);
    }

    public void executeCommand() {
        if (pendingCommand != null) {
            pendingCommand.execute();
            pendingCommand = null;
        }
    }

    public void update(Level level) {
        createCommand(level);
        executeCommand();
    }
}
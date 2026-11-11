package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
/**
 * <b>Application Service</b>. Wires a tank to a control strategy.
 * <p>Uses a two-step lifecycle (createCommand + executeCommand) so that all
 * commands are created first and only then executed — matching the 4-step game loop.</p>
 * <p>Skips work for tanks that have been removed from the level.</p>
 */
public class TankController {
    private final Tank tank;
    private final TankControlStrategy strategy;
    private Command pendingCommand;

    public TankController(Tank tank, TankControlStrategy strategy) {
        this.tank = tank;
        this.strategy = strategy;
    }

    public void createCommand(Level level) {
        if (tank.isRemoved()) {
            pendingCommand = null;
            return;
        }
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
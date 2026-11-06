package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.graphics.HealthBarDisplay;

public class ToggleHealthBarsCommand implements Command {
    private final HealthBarDisplay display;

    public ToggleHealthBarsCommand(HealthBarDisplay display) {
        this.display = display;
    }

    @Override
    public void execute() {
        display.toggle();
    }
}
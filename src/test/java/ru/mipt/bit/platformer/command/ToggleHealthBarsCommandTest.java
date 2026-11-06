package ru.mipt.bit.platformer.command;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.graphics.HealthBarDisplay;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleHealthBarsCommandTest {

    @Test
    void executeTogglesDisplay() {
        HealthBarDisplay display = new HealthBarDisplay();
        ToggleHealthBarsCommand command = new ToggleHealthBarsCommand(display);

        command.execute();
        assertTrue(display.isVisible());

        command.execute();
        assertFalse(display.isVisible());
    }
}
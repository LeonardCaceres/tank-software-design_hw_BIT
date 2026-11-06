package ru.mipt.bit.platformer.graphics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthBarDisplayTest {

    @Test
    void initiallyHidden() {
        HealthBarDisplay display = new HealthBarDisplay();
        assertFalse(display.isVisible());
    }

    @Test
    void toggleChangesState() {
        HealthBarDisplay display = new HealthBarDisplay();
        display.toggle();
        assertTrue(display.isVisible());
        display.toggle();
        assertFalse(display.isVisible());
    }

    @Test
    void toggleIsIdempotentPerClick() {
        HealthBarDisplay display = new HealthBarDisplay();
        display.toggle();
        display.toggle();
        display.toggle();
        assertTrue(display.isVisible());
    }
}
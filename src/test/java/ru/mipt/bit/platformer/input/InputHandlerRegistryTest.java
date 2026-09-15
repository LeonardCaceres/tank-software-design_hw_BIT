package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerRegistryTest {

    @Test
    void callsAllRegisteredHandlers() {
        InputHandlerRegistry registry = new InputHandlerRegistry();
        List<String> calls = new ArrayList<>();
        registry.register(() -> calls.add("first"));
        registry.register(() -> calls.add("second"));

        registry.handleAll();

        assertEquals(List.of("first", "second"), calls);
    }

    @Test
    void noHandlersMeansNoCalls() {
        InputHandlerRegistry registry = new InputHandlerRegistry();
        assertDoesNotThrow(registry::handleAll);
    }
}
package ru.mipt.bit.platformer.input;

import java.util.ArrayList;
import java.util.List;

public class InputHandlerRegistry {
    private final List<InputHandler> handlers = new ArrayList<>();

    public void register(InputHandler handler) {
        handlers.add(handler);
    }

    public void handleAll() {
        for (InputHandler handler : handlers) {
            handler.handle();
        }
    }
}
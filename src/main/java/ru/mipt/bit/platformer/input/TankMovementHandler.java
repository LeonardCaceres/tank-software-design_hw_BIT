package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Map;

public class TankMovementHandler implements InputHandler {
    private final InputSource input;
    private final Tank tank;
    private final Field field;
    private final Map<Integer, Direction> keyBindings;

    public TankMovementHandler(InputSource input,
                               Tank tank,
                               Field field,
                               Map<Integer, Direction> keyBindings) {
        this.input = input;
        this.tank = tank;
        this.field = field;
        this.keyBindings = keyBindings;
    }

    @Override
    public void handle() {
        for (Map.Entry<Integer, Direction> entry : keyBindings.entrySet()) {
            if (input.isKeyPressed(entry.getKey())) {
                tank.move(entry.getValue(), field);
                return;
            }
        }
    }
}
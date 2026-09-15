package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Tank;

import static com.badlogic.gdx.Input.Keys.*;

public class TankMovementHandler implements InputHandler {
    private final InputSource input;
    private final Tank tank;
    private final Field field;

    public TankMovementHandler(InputSource input, Tank tank, Field field) {
        this.input = input;
        this.tank = tank;
        this.field = field;
    }

    @Override
    public void handle() {
        Direction direction = readDirection();
        if (direction != null) {
            tank.move(direction, field);
        }
    }

    private Direction readDirection() {
        if (input.isKeyPressed(UP)    || input.isKeyPressed(W)) return Direction.UP;
        if (input.isKeyPressed(DOWN)  || input.isKeyPressed(S)) return Direction.DOWN;
        if (input.isKeyPressed(LEFT)  || input.isKeyPressed(A)) return Direction.LEFT;
        if (input.isKeyPressed(RIGHT) || input.isKeyPressed(D)) return Direction.RIGHT;
        return null;
    }
}
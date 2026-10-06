package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.I;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankMovementHandlerTest {

    private static final float SPEED = 0.4f;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;

    private static InputSource fakeInput(Integer... pressedKeys) {
        Set<Integer> pressed = Set.of(pressedKeys);
        return pressed::contains;
    }

    private static Map<Integer, Direction> defaultBindings() {
        Map<Integer, Direction> b = new HashMap<>();
        b.put(UP, Direction.UP);
        b.put(W, Direction.UP);
        b.put(DOWN, Direction.DOWN);
        b.put(S, Direction.DOWN);
        b.put(LEFT, Direction.LEFT);
        b.put(A, Direction.LEFT);
        b.put(RIGHT, Direction.RIGHT);
        b.put(D, Direction.RIGHT);
        return b;
    }

    @Test
    void noKeyPressed_doesNotChangeDirection() {
        Tank tank = new Tank(new Position(0, 0), SPEED);
        new TankMovementHandler(fakeInput(), tank, new Field(WIDTH, HEIGHT), defaultBindings()).handle();
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void upArrow_movesTankUp() {
        Tank tank = new Tank(new Position(0, 0), SPEED);
        new TankMovementHandler(fakeInput(UP), tank, new Field(WIDTH, HEIGHT), defaultBindings()).handle();
        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new Position(0, 1), tank.getDestinationPosition());
    }

    @Test
    void wKey_worksLikeUpArrow() {
        Tank tank = new Tank(new Position(0, 0), SPEED);
        new TankMovementHandler(fakeInput(W), tank, new Field(WIDTH, HEIGHT), defaultBindings()).handle();
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void leftAndRight_work() {
        Tank t1 = new Tank(new Position(1, 1), SPEED);
        new TankMovementHandler(fakeInput(LEFT), t1, new Field(WIDTH, HEIGHT), defaultBindings()).handle();
        assertEquals(Direction.LEFT, t1.getDirection());
        assertEquals(new Position(0, 1), t1.getDestinationPosition());

        Tank t2 = new Tank(new Position(1, 1), SPEED);
        new TankMovementHandler(fakeInput(RIGHT), t2, new Field(WIDTH, HEIGHT), defaultBindings()).handle();
        assertEquals(Direction.RIGHT, t2.getDirection());
        assertEquals(new Position(2, 1), t2.getDestinationPosition());
    }

    @Test
    void obstacleBlocksMovement() {
        Field field = new Field(WIDTH, HEIGHT);
        field.addTree(new Tree(new Position(0, 1)));
        Tank tank = new Tank(new Position(0, 0), SPEED);
        new TankMovementHandler(fakeInput(UP), tank, field, defaultBindings()).handle();
        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new Position(0, 0), tank.getDestinationPosition());
        assertTrue(tank.isAtDestination());
    }

    @Test
    void customBindingForNewKeyWorksWithoutChangingHandler() {
        Map<Integer, Direction> custom = new HashMap<>();
        custom.put(I, Direction.UP);

        Tank tank = new Tank(new Position(0, 0), SPEED);
        new TankMovementHandler(fakeInput(I), tank, new Field(WIDTH, HEIGHT), custom).handle();
        assertEquals(Direction.UP, tank.getDirection());
    }
}
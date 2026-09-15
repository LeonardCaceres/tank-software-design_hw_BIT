package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Set;

import static com.badlogic.gdx.Input.Keys.*;
import static org.junit.jupiter.api.Assertions.*;

class TankMovementHandlerTest {

    private static InputSource fakeInput(Integer... pressedKeys) {
        Set<Integer> pressed = Set.of(pressedKeys);
        return pressed::contains;
    }

    @Test
    void noKeyPressed_doesNotChangeDirection() {
        Tank tank = new Tank(new GridPoint2(0, 0));
        new TankMovementHandler(fakeInput(), tank, new Field()).handle();
        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void upArrow_movesTankUp() {
        Tank tank = new Tank(new GridPoint2(0, 0));
        new TankMovementHandler(fakeInput(UP), tank, new Field()).handle();
        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
    }

    @Test
    void wKey_worksLikeUpArrow() {
        Tank tank = new Tank(new GridPoint2(0, 0));
        new TankMovementHandler(fakeInput(W), tank, new Field()).handle();
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void obstacleBlocksMovement() {
        Field field = new Field();
        field.addTree(new Tree(new GridPoint2(0, 1)));
        Tank tank = new Tank(new GridPoint2(0, 0));
        new TankMovementHandler(fakeInput(UP), tank, field).handle();
        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(0, 0), tank.getDestinationCoordinates());
        assertTrue(tank.isAtDestination());
    }
}
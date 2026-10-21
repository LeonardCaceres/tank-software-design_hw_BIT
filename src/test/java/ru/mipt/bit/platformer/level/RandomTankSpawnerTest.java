package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RandomTankSpawnerTest {

    private static final float SPEED = 0.4f;

    @Test
    void spawnsRequestedNumberOfTanks() {
        Level level = new Level(new Field(10, 8), new Position(0, 0));
        level.addTank(new Tank(new Position(0, 0), SPEED));

        List<Tank> spawned = new RandomTankSpawner(new Random(42))
                .spawn(level, 3, SPEED);

        assertEquals(3, spawned.size());
    }

    @Test
    void tanksDoNotOverlapWithTreesOrEachOther() {
        Field field = new Field(10, 8);

        for (int x = 0; x < 10; x++) {
            field.addTree(new Tree(new Position(x, 0)));
        }
        Level level = new Level(field, new Position(5, 5));
        level.addTank(new Tank(new Position(5, 5), SPEED));

        List<Tank> spawned = new RandomTankSpawner(new Random(1))
                .spawn(level, 3, SPEED);

        Set<Position> positions = new HashSet<>();
        for (Tank t : spawned) {
            Position p = t.getPosition();
            assertEquals(true, positions.add(p), "duplicate position: " + p);
            assertFalse(p.getY() == 0, "tank on tree row: " + p);
            assertFalse(p.equals(new Position(5, 5)), "tank on player start");
        }
    }

    @Test
    void sameSeedProducesSameResult() {
        Level l1 = new Level(new Field(10, 8), new Position(0, 0));
        l1.addTank(new Tank(new Position(0, 0), SPEED));
        List<Tank> a = new RandomTankSpawner(new Random(99)).spawn(l1, 3, SPEED);

        Level l2 = new Level(new Field(10, 8), new Position(0, 0));
        l2.addTank(new Tank(new Position(0, 0), SPEED));
        List<Tank> b = new RandomTankSpawner(new Random(99)).spawn(l2, 3, SPEED);

        assertEquals(a.size(), b.size());
        for (int i = 0; i < a.size(); i++) {
            assertEquals(a.get(i).getPosition(), b.get(i).getPosition());
        }
    }
}
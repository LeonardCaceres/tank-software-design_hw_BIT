package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelSourceTest {

    @Test
    void sameSeedProducesSameLevel() {
        Level a = new RandomLevelSource(10, 8, 8, new Random(42)).load();
        Level b = new RandomLevelSource(10, 8, 8, new Random(42)).load();

        assertEquals(a.getTankStart(), b.getTankStart());
        assertEquals(a.getField().getTrees(), b.getField().getTrees());
    }

    @Test
    void placesRequestedNumberOfTrees() {
        Level level = new RandomLevelSource(10, 8, 8, new Random(1)).load();
        assertEquals(8, level.getField().getTrees().size());
    }

    @Test
    void tankStartDoesNotOverlapWithTrees() {
        Level level = new RandomLevelSource(10, 8, 8, new Random(7)).load();
        assertFalse(level.getField().isObstacle(level.getTankStart()));
    }

    @Test
    void allPositionsAreWithinBounds() {
        int w = 10;
        int h = 8;
        Level level = new RandomLevelSource(w, h, 8, new Random(3)).load();

        Set<Position> all = new HashSet<>();
        level.getField().getTrees().forEach(t -> all.add(t.getPosition()));
        all.add(level.getTankStart());

        for (Position p : all) {
            assertTrue(p.getX() >= 0 && p.getX() < w, "x out of bounds: " + p);
            assertTrue(p.getY() >= 0 && p.getY() < h, "y out of bounds: " + p);
        }
    }

    @Test
    void tooManyTreesThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomLevelSource(3, 3, 9, new Random()));
    }
}
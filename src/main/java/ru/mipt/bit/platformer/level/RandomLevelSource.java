package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
/**
 * <b>Adapter</b> (driven). Random level generator.
 * <p>Tree positions and tank start are chosen randomly and never overlap.
 * Trees are added via {@link Level#addTree} so observer notifications are not
 * bypassed (matches the "single owner of level mutations" contract of {@link Level}).</p>
 */
public class RandomLevelSource implements LevelSource {
    private final int width;
    private final int height;
    private final int treeCount;
    private final Random random;

    public RandomLevelSource(int width, int height, int treeCount, Random random) {
        if (treeCount >= width * height) {
            throw new IllegalArgumentException(
                    "treeCount must be less than width*height (need a free cell for the tank)");
        }
        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
        this.random = random;
    }

    @Override
    public Level load() {
        Set<Position> occupied = new HashSet<>();

        Position tankStart = nextFreePosition(occupied);
        occupied.add(tankStart);

        List<Position> treePositions = new ArrayList<>();
        for (int i = 0; i < treeCount; i++) {
            Position p = nextFreePosition(occupied);
            occupied.add(p);
            treePositions.add(p);
        }

        Field field = new Field(width, height);
        Level level = new Level(field, tankStart);
        for (Position p : treePositions) {
            level.addTree(new Tree(p));
        }
        return level;
    }

    private Position nextFreePosition(Set<Position> occupied) {
        while (true) {
            Position p = new Position(random.nextInt(width), random.nextInt(height));
            if (!occupied.contains(p)) {
                return p;
            }
        }
    }
}
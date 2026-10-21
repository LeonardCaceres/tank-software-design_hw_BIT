package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tree;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
        Field field = new Field(width, height);
        Set<Position> occupied = new HashSet<>();

        for (int i = 0; i < treeCount; i++) {
            Position p = nextFreePosition(occupied);
            occupied.add(p);
            field.addTree(new Tree(p));
        }

        Position tankStart = nextFreePosition(occupied);
        return new Level(field, tankStart);
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
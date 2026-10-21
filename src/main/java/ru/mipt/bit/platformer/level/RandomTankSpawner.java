package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTankSpawner {
    private final Random random;

    public RandomTankSpawner(Random random) {
        this.random = random;
    }

    public List<Tank> spawn(Level level, int count, float movementSpeed) {
        List<Tank> spawned = new ArrayList<>();
        int maxAttempts = level.getField().getWidth()
                * level.getField().getHeight() * 10;

        for (int i = 0; i < count; i++) {
            Tank tank = trySpawnOne(level, spawned, movementSpeed, maxAttempts);
            if (tank == null) {
                break;
            }
            spawned.add(tank);
        }
        return spawned;
    }

    private Tank trySpawnOne(Level level, List<Tank> alreadySpawned,
                             float speed, int maxAttempts) {
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = random.nextInt(level.getField().getWidth());
            int y = random.nextInt(level.getField().getHeight());
            Position position = new Position(x, y);
            if (isFree(position, level, alreadySpawned)) {
                return new Tank(position, speed);
            }
        }
        return null;
    }

    private boolean isFree(Position position, Level level,
                           List<Tank> additionalTanks) {
        if (level.getField().isObstacle(position)) {
            return false;
        }
        if (position.equals(level.getTankStart())) {
            return false;
        }
        for (Tank t : level.getTanks()) {
            if (t.getPosition().equals(position)) {
                return false;
            }
        }
        for (Tank t : additionalTanks) {
            if (t.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }
}
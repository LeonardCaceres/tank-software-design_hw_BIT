package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {
    private final Field field;
    private final Position tankStart;
    private final List<Tank> tanks = new ArrayList<>();

    public Level(Field field, Position tankStart) {
        this.field = field;
        this.tankStart = tankStart;
    }

    public Field getField() {
        return field;
    }

    public Position getTankStart() {
        return tankStart;
    }

    public void addTank(Tank tank) {
        tanks.add(tank);
    }

    public List<Tank> getTanks() {
        return Collections.unmodifiableList(tanks);
    }

    public boolean isWithinBounds(Position position) {
        return position.getX() >= 0 && position.getX() < field.getWidth()
                && position.getY() >= 0 && position.getY() < field.getHeight();
    }

    public boolean canMove(Tank tank, Position position) {
        if (!isWithinBounds(position)) {
            return false;
        }
        if (field.isObstacle(position)) {
            return false;
        }
        for (Tank other : tanks) {
            if (other == tank) {
                continue;
            }
            if (other.getPosition().equals(position)) {
                return false;
            }
            if (other.getDestinationPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }
}
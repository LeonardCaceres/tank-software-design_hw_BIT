package ru.mipt.bit.platformer.model;

public class Level {
    private final Field field;
    private final Position tankStart;

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
}
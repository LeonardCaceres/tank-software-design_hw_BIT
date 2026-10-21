package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tree;

import java.util.List;

public class LevelParser {

    public Level parse(List<String> lines) {
        int height = lines.size();
        int width = lines.stream().mapToInt(String::length).max().orElse(0);

        Field field = new Field(width, height);
        Position tankStart = null;

        for (int row = 0; row < height; row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                int modelY = height - 1 - row;
                Position position = new Position(col, modelY);

                switch (c) {
                    case 'T':
                        field.addTree(new Tree(position));
                        break;
                    case 'X':
                        tankStart = position;
                        break;
                    case '_':
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Unknown level char '" + c + "' at (col=" + col + ", row=" + row + ")");
                }
            }
        }

        if (tankStart == null) {
            throw new IllegalStateException("Level has no 'X' (tank start)");
        }
        return new Level(field, tankStart);
    }
}
package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Application Service</b>. Parses level text into a {@link Level}.
 * <p>Format: {@code '_'} = empty, {@code 'T'} = tree, {@code 'X'} = tank start
 * (must be exactly one). String index 0 corresponds to the top row, so
 * {@code modelY = height - 1 - row}. Trees are added via {@link Level#addTree}
 * so observer notifications are not bypassed.</p>
 */
public class LevelParser {

    public Level parse(List<String> lines) {
        int height = lines.size();
        int width = lines.stream().mapToInt(String::length).max().orElse(0);

        List<Position> treePositions = new ArrayList<>();
        Position tankStart = null;

        for (int row = 0; row < height; row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                int modelY = height - 1 - row;
                Position position = new Position(col, modelY);

                switch (c) {
                    case 'T':
                        treePositions.add(position);
                        break;
                    case 'X':
                        if (tankStart != null) {
                            throw new IllegalArgumentException(
                                    "Level has more than one 'X' (tank start): found second at "
                                            + "(col=" + col + ", row=" + row + ")");
                        }
                        tankStart = position;
                        break;
                    case '_':
                        break;
                    default:
                        throw new IllegalArgumentException(
                                "Unknown level char '" + c + "' at (col=" + col
                                        + ", row=" + row + ")");
                }
            }
        }

        if (tankStart == null) {
            throw new IllegalStateException("Level has no 'X' (tank start)");
        }

        Field field = new Field(width, height);
        Level level = new Level(field, tankStart);
        for (Position p : treePositions) {
            level.addTree(new Tree(p));
        }
        return level;
    }
}
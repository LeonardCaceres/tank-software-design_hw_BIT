package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Position;
import ru.mipt.bit.platformer.model.Tree;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelParserTest {

    private final LevelParser parser = new LevelParser();

    @Test
    void parsesTankStartAndTrees() {
        Level level = parser.parse(List.of(
                "_T_",
                "_X_"
        ));

        assertEquals(new Position(1, 0), level.getTankStart());
        assertEquals(1, level.getField().getTrees().size());
        assertTrue(level.getField().getTrees().contains(new Tree(new Position(1, 1))));
    }

    @Test
    void fieldDimensionsMatchInput() {
        Level level = parser.parse(List.of(
                "_____",
                "_T_X_",
                "_____"
        ));
        assertEquals(5, level.getField().getWidth());
        assertEquals(3, level.getField().getHeight());
    }

    @Test
    void missingTankStartThrows() {
        assertThrows(IllegalStateException.class,
                () -> parser.parse(List.of("T_T", "___")));
    }

    @Test
    void unknownCharThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse(List.of("XZ_")));
    }

    @Test
    void yAxisIsFlipped() {
        Level bottom = parser.parse(List.of(
                "___",
                "_X_"
        ));
        assertEquals(new Position(1, 0), bottom.getTankStart());

        Level top = parser.parse(List.of(
                "_X_",
                "___"
        ));
        assertEquals(new Position(1, 1), top.getTankStart());
    }
}
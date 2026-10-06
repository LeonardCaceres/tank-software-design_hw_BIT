package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.Level;

import java.util.List;

public class FileLevelSource implements LevelSource {
    private final List<String> lines;
    private final LevelParser parser;

    public FileLevelSource(List<String> lines) {
        this.lines = lines;
        this.parser = new LevelParser();
    }

    @Override
    public Level load() {
        return parser.parse(lines);
    }
}
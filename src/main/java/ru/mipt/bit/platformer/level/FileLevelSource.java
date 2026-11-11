package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.Level;

import java.util.List;
import java.util.function.Supplier;

/**
 * <b>Adapter</b> (driven). Supplies level from a text source.
 * <p>Takes a {@link Supplier} so file/IO is decoupled: caller (composition root)
 * is responsible for reading lines, this class only parses them. Testable without
 * touching the filesystem.</p>
 */
public class FileLevelSource implements LevelSource {
    private final Supplier<List<String>> linesSupplier;
    private final LevelParser parser;

    public FileLevelSource(Supplier<List<String>> linesSupplier) {
        this.linesSupplier = linesSupplier;
        this.parser = new LevelParser();
    }

    public FileLevelSource(List<String> lines) {
        this(() -> lines);
    }

    @Override
    public Level load() {
        return parser.parse(linesSupplier.get());
    }
}
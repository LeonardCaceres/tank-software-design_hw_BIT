package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Field {
    private final int width;
    private final int height;
    private final List<Tree> trees = new ArrayList<>();

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addTree(Tree tree) {
        trees.add(tree);
    }

    public List<Tree> getTrees() {
        return Collections.unmodifiableList(trees);
    }

    public boolean isObstacle(Position position) {
        for (Tree tree : trees) {
            if (tree.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }
}
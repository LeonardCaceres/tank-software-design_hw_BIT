package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.List;

public class GameRenderer {
    private final LevelRenderer levelRenderer;
    private final List<TankRenderer> tankRenderers;
    private final List<TreeRenderer> treeRenderers;

    public GameRenderer(LevelRenderer levelRenderer,
                        List<TankRenderer> tankRenderers,
                        List<TreeRenderer> treeRenderers) {
        this.levelRenderer = levelRenderer;
        this.tankRenderers = tankRenderers;
        this.treeRenderers = treeRenderers;
    }

    public void render(Batch batch) {
        levelRenderer.render();

        batch.begin();
        for (TankRenderer tr : tankRenderers) {
            tr.render(batch);
        }
        for (TreeRenderer tr : treeRenderers) {
            tr.render(batch);
        }
        batch.end();
    }

    public void dispose() {
        levelRenderer.dispose();
    }
}
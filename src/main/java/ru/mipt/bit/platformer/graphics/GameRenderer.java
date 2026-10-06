package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

public class GameRenderer {
    private final LevelRenderer levelRenderer;
    private final TankRenderer tankRenderer;
    private final List<TreeRenderer> treeRenderers;

    public GameRenderer(LevelRenderer levelRenderer,
                        TankRenderer tankRenderer,
                        List<TreeRenderer> treeRenderers) {
        this.levelRenderer = levelRenderer;
        this.tankRenderer = tankRenderer;
        this.treeRenderers = treeRenderers;
    }

    public void render(Batch batch, Tank tank) {
        levelRenderer.render();

        batch.begin();
        tankRenderer.render(batch, tank);
        for (TreeRenderer treeRenderer : treeRenderers) {
            treeRenderer.render(batch);
        }
        batch.end();
    }

    public void dispose() {
        levelRenderer.dispose();
    }
}
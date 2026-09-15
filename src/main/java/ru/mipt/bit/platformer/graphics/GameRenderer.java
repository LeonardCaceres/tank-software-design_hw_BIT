package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mipt.bit.platformer.model.Tank;

public class GameRenderer {
    private final LevelRenderer levelRenderer;
    private final TankRenderer tankRenderer;
    private final TreeRenderer treeRenderer;

    public GameRenderer(LevelRenderer levelRenderer,
                        TankRenderer tankRenderer,
                        TreeRenderer treeRenderer) {
        this.levelRenderer = levelRenderer;
        this.tankRenderer = tankRenderer;
        this.treeRenderer = treeRenderer;
    }

    public void render(Batch batch, Tank tank) {
        levelRenderer.render();

        batch.begin();
        tankRenderer.render(batch, tank);
        treeRenderer.render(batch);
        batch.end();
    }

    public void dispose() {
        levelRenderer.dispose();
    }
}
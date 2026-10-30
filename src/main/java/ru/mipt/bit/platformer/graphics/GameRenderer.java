package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.LevelObserver;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameRenderer implements LevelObserver {
    private final LevelRenderer levelRenderer;
    private final TextureRegion tankTexture;
    private final TextureRegion treeTexture;
    private final TextureRegion bulletTexture;
    private final TileMovement tileMovement;

    private final Map<Tank, TankRenderer> tankRenderers = new LinkedHashMap<>();
    private final Map<Tree, TreeRenderer> treeRenderers = new LinkedHashMap<>();
    private final Map<Bullet, BulletRenderer> bulletRenderers = new LinkedHashMap<>();

    public GameRenderer(LevelRenderer levelRenderer,
                        TextureRegion tankTexture,
                        TextureRegion treeTexture,
                        TextureRegion bulletTexture) {
        this.levelRenderer = levelRenderer;
        this.tankTexture = tankTexture;
        this.treeTexture = treeTexture;
        this.bulletTexture = bulletTexture;
        this.tileMovement = levelRenderer.getTileMovement();
    }

    @Override
    public void onTankAdded(Tank tank) {
        tankRenderers.put(tank, new TankRenderer(tankTexture, tileMovement, tank));
    }

    @Override
    public void onTankRemoved(Tank tank) {
        tankRenderers.remove(tank);
    }

    @Override
    public void onBulletAdded(Bullet bullet) {
        bulletRenderers.put(bullet, new BulletRenderer(bulletTexture, tileMovement, bullet));
    }

    @Override
    public void onBulletRemoved(Bullet bullet) {
        bulletRenderers.remove(bullet);
    }

    @Override
    public void onTreeAdded(Tree tree) {
        treeRenderers.put(tree, new TreeRenderer(
                treeTexture, levelRenderer.getGroundLayer(), tree.getPosition()));
    }

    @Override
    public void onTreeRemoved(Tree tree) {
        treeRenderers.remove(tree);
    }

    public void render(Batch batch) {
        levelRenderer.render();
        batch.begin();
        for (TreeRenderer r : treeRenderers.values()) {
            r.render(batch);
        }
        for (TankRenderer r : tankRenderers.values()) {
            r.render(batch);
        }
        for (BulletRenderer r : bulletRenderers.values()) {
            r.render(batch);
        }
        batch.end();
    }

    public void dispose() {
        levelRenderer.dispose();
    }
}
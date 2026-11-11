package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.LevelObserver;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * <b>Adapter</b> (driven, infrastructure). Listens to {@link LevelObserver}
 * events and renders all entities via libGDX.
 * <p>All entity renderers go through a single {@link EntityRenderer} abstraction;
 * z-order is defined by {@link EntityRenderer#getLayer()}. Specific renderers
 * (tank/tree/bullet) are still separate adapters, but the coordinator does not
 * depend on their concrete types — only on {@code EntityRenderer}.</p>
 */
public class GameRenderer implements LevelObserver {
    private final LevelRenderer levelRenderer;
    private final TextureRegion tankTexture;
    private final TextureRegion treeTexture;
    private final TextureRegion bulletTexture;
    private final HealthBarDisplay healthBarDisplay;
    private final Texture healthBarPixel;
    private final TileMovement tileMovement;

    private final Map<Object, EntityRenderer> entityRenderers = new LinkedHashMap<>();

    public GameRenderer(LevelRenderer levelRenderer,
                        TextureRegion tankTexture,
                        TextureRegion treeTexture,
                        TextureRegion bulletTexture,
                        HealthBarDisplay healthBarDisplay,
                        Texture healthBarPixel) {
        this.levelRenderer = levelRenderer;
        this.tankTexture = tankTexture;
        this.treeTexture = treeTexture;
        this.bulletTexture = bulletTexture;
        this.healthBarDisplay = healthBarDisplay;
        this.healthBarPixel = healthBarPixel;
        this.tileMovement = levelRenderer.getTileMovement();
    }

    @Override
    public void onTankAdded(Tank tank) {
        TankRenderer body = new SimpleTankRenderer(tankTexture, tileMovement, tank);
        entityRenderers.put(tank, new HealthBarTankRenderer(body, tank, healthBarDisplay, healthBarPixel));
    }

    @Override
    public void onTankRemoved(Tank tank) {
        entityRenderers.remove(tank);
    }

    @Override
    public void onBulletAdded(Bullet bullet) {
        entityRenderers.put(bullet, new BulletRenderer(bulletTexture, tileMovement, bullet));
    }

    @Override
    public void onBulletRemoved(Bullet bullet) {
        entityRenderers.remove(bullet);
    }

    @Override
    public void onObstacleAdded(Obstacle obstacle) {
        if (obstacle instanceof Tree) {
            Tree tree = (Tree) obstacle;
            entityRenderers.put(tree, new TreeRenderer(
                    treeTexture, levelRenderer.getGroundLayer(), tree.getPosition()));
        }

    }

    @Override
    public void onObstacleRemoved(Obstacle obstacle) {
        entityRenderers.remove(obstacle);
    }

    public void render(Batch batch) {
        levelRenderer.render();
        batch.begin();
        List<EntityRenderer> sorted = new ArrayList<>(entityRenderers.values());
        sorted.sort(Comparator.comparingInt(EntityRenderer::getLayer));
        for (EntityRenderer renderer : sorted) {
            renderer.render(batch);
        }
        batch.end();
    }

    public void dispose() {
        levelRenderer.dispose();
    }
}
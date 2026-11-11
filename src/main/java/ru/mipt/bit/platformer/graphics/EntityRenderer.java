package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * <b>Port</b> (within graphics infrastructure). Common abstraction for anything
 * that can draw itself through {@link Batch}.
 * <p>Replaces per-entity renderer interfaces (TankRenderer, TreeRenderer, BulletRenderer)
 * with a single contract. {@link TankRenderer} remains as a specialised sub-interface
 * for decorators that need render bounds (e.g. health bars).</p>
 * <p>{@link #getLayer()} defines z-order: lower values are rendered first.</p>
 */
public interface EntityRenderer {
    void render(Batch batch);

    default int getLayer() {
        return 0;
    }
}
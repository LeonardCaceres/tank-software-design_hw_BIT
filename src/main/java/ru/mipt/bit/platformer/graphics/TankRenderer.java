package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
/**
 * <b>Port</b> (within graphics infrastructure). Specialisation of {@link EntityRenderer}
 * for tank renderers that expose their on-screen bounds (used by decorators such as
 * {@link HealthBarTankRenderer}).
 */
public interface TankRenderer extends EntityRenderer {
    Rectangle getRenderBounds();

    @Override
    default int getLayer() {
        return 1;
    }
}
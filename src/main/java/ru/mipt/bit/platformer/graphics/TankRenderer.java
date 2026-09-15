package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class TankRenderer {
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final TileMovement tileMovement;

    public TankRenderer(TextureRegion graphics, TileMovement tileMovement) {
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
        this.tileMovement = tileMovement;
    }

    public void render(Batch batch, Tank tank) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle,
                tank.getCoordinates(),
                tank.getDestinationCoordinates(),
                tank.getMovementProgress());

        drawTextureRegionUnscaled(batch, graphics, rectangle,
                tank.getDirection().getRotation());
    }
}
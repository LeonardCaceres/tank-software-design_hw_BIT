package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class TankRenderer {
    private final Tank tank;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final TileMovement tileMovement;

    public TankRenderer(TextureRegion graphics,
                        TileMovement tileMovement,
                        Tank tank) {
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
        this.tileMovement = tileMovement;
        this.tank = tank;
    }

    public void render(Batch batch) {
        GridPoint2 from = new GridPoint2(
                tank.getPosition().getX(), tank.getPosition().getY());
        GridPoint2 to = new GridPoint2(
                tank.getDestinationPosition().getX(),
                tank.getDestinationPosition().getY());

        tileMovement.moveRectangleBetweenTileCenters(
                rectangle, from, to, tank.getMovementProgress());

        drawTextureRegionUnscaled(batch, graphics, rectangle,
                tank.getDirection().getRotation());
    }
}
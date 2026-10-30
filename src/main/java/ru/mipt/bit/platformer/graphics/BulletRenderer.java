package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class BulletRenderer {
    private final Bullet bullet;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final TileMovement tileMovement;

    public BulletRenderer(TextureRegion graphics,
                          TileMovement tileMovement,
                          Bullet bullet) {
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
        this.tileMovement = tileMovement;
        this.bullet = bullet;
    }

    public void render(Batch batch) {
        GridPoint2 from = new GridPoint2(
                bullet.getPosition().getX(), bullet.getPosition().getY());
        GridPoint2 to = new GridPoint2(
                bullet.getDestinationPosition().getX(),
                bullet.getDestinationPosition().getY());

        tileMovement.moveRectangleBetweenTileCenters(
                rectangle, from, to, bullet.getMovementProgress());

        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }
}
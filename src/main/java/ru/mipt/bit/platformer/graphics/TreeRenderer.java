package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Position;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TreeRenderer {
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public TreeRenderer(TextureRegion graphics,
                        TiledMapTileLayer groundLayer,
                        Position position) {
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
        GridPoint2 gdx = new GridPoint2(position.getX(), position.getY());
        moveRectangleAtTileCenter(groundLayer, rectangle, gdx);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }
}
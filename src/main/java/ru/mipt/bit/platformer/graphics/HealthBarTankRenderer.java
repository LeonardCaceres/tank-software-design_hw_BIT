package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;

public class HealthBarTankRenderer implements TankRenderer {
    private final TankRenderer delegate;
    private final Tank tank;
    private final HealthBarDisplay display;
    private final Texture pixel;

    public HealthBarTankRenderer(TankRenderer delegate,
                                 Tank tank,
                                 HealthBarDisplay display,
                                 Texture pixel) {
        this.delegate = delegate;
        this.tank = tank;
        this.display = display;
        this.pixel = pixel;
    }

    @Override
    public void render(Batch batch) {
        delegate.render(batch);
        if (!display.isVisible()) {
            return;
        }
        drawHealthBar(batch);
    }

    @Override
    public Rectangle getRenderBounds() {
        return delegate.getRenderBounds();
    }

    private void drawHealthBar(Batch batch) {
        Rectangle bounds = delegate.getRenderBounds();

        float barWidth = bounds.width * 0.8f;
        float barHeight = Math.max(4f, bounds.height * 0.1f);
        float barX = bounds.x + (bounds.width - barWidth) / 2f;
        float barY = bounds.y + bounds.height + 3f;

        batch.setColor(0.2f, 0.2f, 0.2f, 0.9f);
        batch.draw(pixel, barX, barY, barWidth, barHeight);

        float ratio = (float) tank.getHealth() / tank.getMaxHealth();
        ratio = Math.max(0f, Math.min(1f, ratio));

        if (ratio > 0.5f) {
            batch.setColor(0f, 0.8f, 0f, 1f);
        } else if (ratio > 0.25f) {
            batch.setColor(1f, 0.7f, 0f, 1f);
        } else {
            batch.setColor(1f, 0.2f, 0.2f, 1f);
        }
        batch.draw(pixel, barX, barY, barWidth * ratio, barHeight);

        batch.setColor(1f, 1f, 1f, 1f);
    }
}
package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.graphics.TankRenderer;
import ru.mipt.bit.platformer.graphics.TreeRenderer;
import ru.mipt.bit.platformer.input.GdxInputSource;
import ru.mipt.bit.platformer.input.InputHandlerRegistry;
import ru.mipt.bit.platformer.input.InputSource;
import ru.mipt.bit.platformer.input.TankMovementHandler;
import ru.mipt.bit.platformer.level.FileLevelSource;
import ru.mipt.bit.platformer.level.LevelSource;
import ru.mipt.bit.platformer.level.RandomLevelSource;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final boolean USE_RANDOM_LEVEL = false;

    private Batch batch;
    private Field field;
    private Tank tank;
    private InputHandlerRegistry inputHandlers;
    private GameRenderer renderer;

    private Texture blueTankTexture;
    private Texture greenTreeTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();

        TiledMap level = new TmxMapLoader().load("level.tmx");
        LevelRenderer levelRenderer = new LevelRenderer(level, batch);

        blueTankTexture = new Texture("images/tank_blue.png");
        greenTreeTexture = new Texture("images/greenTree.png");

        LevelSource levelSource = createLevelSource();
        Level gameLevel = levelSource.load();

        field = gameLevel.getField();
        tank = new Tank(gameLevel.getTankStart(), TANK_MOVEMENT_SPEED);

        TankRenderer tankRenderer = new TankRenderer(
                new TextureRegion(blueTankTexture),
                levelRenderer.getTileMovement());

        List<TreeRenderer> treeRenderers = new ArrayList<>();
        for (Tree tree : field.getTrees()) {
            treeRenderers.add(new TreeRenderer(
                    new TextureRegion(greenTreeTexture),
                    levelRenderer.getGroundLayer(),
                    tree.getPosition()));
        }

        renderer = new GameRenderer(levelRenderer, tankRenderer, treeRenderers);

        InputSource input = new GdxInputSource();
        inputHandlers = new InputHandlerRegistry();
        inputHandlers.register(new TankMovementHandler(
                input, tank, field, buildKeyBindings()));
    }

    private LevelSource createLevelSource() {
        if (USE_RANDOM_LEVEL) {
            return new RandomLevelSource(10, 8, 8, new Random());
        }
        String content = Gdx.files.internal("levels/level1.txt").readString();
        return new FileLevelSource(Arrays.asList(content.split("\\r?\\n")));
    }

    private Map<Integer, Direction> buildKeyBindings() {
        Map<Integer, Direction> bindings = new HashMap<>();
        bindings.put(UP, Direction.UP);
        bindings.put(W, Direction.UP);
        bindings.put(DOWN, Direction.DOWN);
        bindings.put(S, Direction.DOWN);
        bindings.put(LEFT, Direction.LEFT);
        bindings.put(A, Direction.LEFT);
        bindings.put(RIGHT, Direction.RIGHT);
        bindings.put(D, Direction.RIGHT);
        return bindings;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        inputHandlers.handleAll();
        tank.update(deltaTime);
        renderer.render(batch, tank);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        renderer.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
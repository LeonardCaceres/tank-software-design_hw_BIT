package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import ru.mipt.bit.platformer.control.AiTankControlStrategy;
import ru.mipt.bit.platformer.control.PlayerTankControlStrategy;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.input.GdxInputSource;
import ru.mipt.bit.platformer.input.InputSource;
import ru.mipt.bit.platformer.level.FileLevelSource;
import ru.mipt.bit.platformer.level.LevelSource;
import ru.mipt.bit.platformer.level.RandomLevelSource;
import ru.mipt.bit.platformer.level.RandomTankSpawner;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final boolean USE_RANDOM_LEVEL = false;
    private static final int AI_TANK_COUNT = 3;

    private Batch batch;
    private Level level;
    private List<TankController> controllers;
    private GameRenderer renderer;

    private Texture blueTankTexture;
    private Texture greenTreeTexture;
    private Texture bulletTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();

        TiledMap tiledMap = new TmxMapLoader().load("level.tmx");
        LevelRenderer levelRenderer = new LevelRenderer(tiledMap, batch);

        blueTankTexture = new Texture("images/tank_blue.png");
        greenTreeTexture = new Texture("images/greenTree.png");
        bulletTexture = createBulletTexture();

        Random random = new Random();
        LevelSource levelSource = createLevelSource(random);
        level = levelSource.load();

        renderer = new GameRenderer(
                levelRenderer,
                new TextureRegion(blueTankTexture),
                new TextureRegion(greenTreeTexture),
                new TextureRegion(bulletTexture));
        level.addObserver(renderer);

        Tank playerTank = new Tank(level.getTankStart(), TANK_MOVEMENT_SPEED);
        level.addTank(playerTank);

        RandomTankSpawner spawner = new RandomTankSpawner(random);
        List<Tank> aiTanks = spawner.spawn(level, AI_TANK_COUNT, TANK_MOVEMENT_SPEED);
        for (Tank aiTank : aiTanks) {
            level.addTank(aiTank);
        }

        InputSource input = new GdxInputSource();
        controllers = new ArrayList<>();
        controllers.add(new TankController(playerTank,
                new PlayerTankControlStrategy(input, buildKeyBindings(), SPACE)));
        for (Tank aiTank : aiTanks) {
            controllers.add(new TankController(aiTank, new AiTankControlStrategy(random)));
        }
    }

    private LevelSource createLevelSource(Random random) {
        if (USE_RANDOM_LEVEL) {
            return new RandomLevelSource(10, 8, 8, random);
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

    private Texture createBulletTexture() {
        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillCircle(16, 16, 8);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        for (TankController controller : controllers) {
            controller.createCommand(level);
        }
        for (TankController controller : controllers) {
            controller.executeCommand();
        }
        level.tick(deltaTime);
        renderer.render(batch);
    }

    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }

    @Override
    public void dispose() {
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        bulletTexture.dispose();
        renderer.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
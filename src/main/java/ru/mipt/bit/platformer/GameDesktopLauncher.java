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
import ru.mipt.bit.platformer.command.ToggleHealthBarsCommand;
import ru.mipt.bit.platformer.control.AiTankControlStrategy;
import ru.mipt.bit.platformer.control.PlayerTankControlStrategy;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.HealthBarDisplay;
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
import static com.badlogic.gdx.Input.Keys.L;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * <b>Infrastructure / Composition Root</b>. Wires all layers together:
 * <ul>
 *   <li>{@link LevelSource} — injected via constructor; chosen in {@link #main(String[])}</li>
 *   <li>{@link InputSource} — adapter to libGDX input</li>
 *   <li>{@link GameRenderer} — adapter to libGDX graphics, subscribed as {@code LevelObserver}</li>
 * </ul>
 * {@code FileContentReader} (wraps Gdx.files), and {@code TextureProvider}
 * (wraps new Texture(...)) into explicit ports — currently these are wired
 * directly in this composition root.</p>
 */
public class GameDesktopLauncher implements ApplicationListener {

    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final int AI_TANK_COUNT = 3;
    private static final int MIN_HEALTH = 90;
    private static final int MAX_HEALTH = 100;
    private static final int BULLET_DAMAGE = 10;
    private static final String DEFAULT_LEVEL_PATH = "levels/level1.txt";

    private final LevelSource levelSource;

    private Batch batch;
    private Level level;
    private List<TankController> controllers;
    private GameRenderer renderer;
    private InputSource input;
    private HealthBarDisplay healthBarDisplay;

    private Texture blueTankTexture;
    private Texture greenTreeTexture;
    private Texture bulletTexture;
    private Texture healthBarPixel;

    public GameDesktopLauncher(LevelSource levelSource) {
        this.levelSource = levelSource;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        TiledMap tiledMap = new TmxMapLoader().load("level.tmx");
        LevelRenderer levelRenderer = new LevelRenderer(tiledMap, batch);

        blueTankTexture = new Texture("images/tank_blue.png");
        greenTreeTexture = new Texture("images/greenTree.png");
        bulletTexture = createBulletTexture();
        healthBarPixel = createPixelTexture();

        healthBarDisplay = new HealthBarDisplay();

        Random random = new Random();
        level = levelSource.load();
        validateLevelSize(level, tiledMap);

        renderer = new GameRenderer(
                levelRenderer,
                new TextureRegion(blueTankTexture),
                new TextureRegion(greenTreeTexture),
                new TextureRegion(bulletTexture),
                healthBarDisplay,
                healthBarPixel);
        level.addObserver(renderer);

        int playerHealth = randomHealth(random);
        Tank playerTank = new Tank(level.getTankStart(),
                TANK_MOVEMENT_SPEED, playerHealth, BULLET_DAMAGE);
        level.addTank(playerTank);

        RandomTankSpawner spawner = new RandomTankSpawner(random);
        List<Tank> aiTanks = spawner.spawn(level, AI_TANK_COUNT, TANK_MOVEMENT_SPEED);
        for (Tank aiTank : aiTanks) {
            level.addTank(aiTank);
        }

        input = new GdxInputSource();
        controllers = new ArrayList<>();
        controllers.add(new TankController(playerTank,
                new PlayerTankControlStrategy(input, buildKeyBindings(), SPACE)));
        for (Tank aiTank : aiTanks) {
            controllers.add(new TankController(aiTank, new AiTankControlStrategy(random)));
        }
    }

    private void validateLevelSize(Level level, TiledMap tiledMap) {
        Integer mapWidth = tiledMap.getProperties().get("width", Integer.class);
        Integer mapHeight = tiledMap.getProperties().get("height", Integer.class);
        if (mapWidth == null || mapHeight == null) return;
        if (level.getField().getWidth() != mapWidth
                || level.getField().getHeight() != mapHeight) {
            Gdx.app.error("GameDesktopLauncher",
                    "Level size mismatch: file=" + level.getField().getWidth()
                            + "x" + level.getField().getHeight()
                            + ", map=" + mapWidth + "x" + mapHeight);
        }
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

    private int randomHealth(Random random) {
        return MIN_HEALTH + random.nextInt(MAX_HEALTH - MIN_HEALTH + 1);
    }

    private Texture createBulletTexture() {
        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillCircle(16, 16, 8);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createPixelTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        if (input.isKeyJustPressed(L)) {
            new ToggleHealthBarsCommand(healthBarDisplay).execute();
        }

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
        healthBarPixel.dispose();
        renderer.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        boolean useRandom = args.length > 0 && "random".equalsIgnoreCase(args[0]);
        LevelSource source = useRandom
                ? new RandomLevelSource(10, 8, 8, new Random())
                : new FileLevelSource(() -> {
            String content = Gdx.files.internal(DEFAULT_LEVEL_PATH).readString();
            return Arrays.asList(content.split("\\r?\\n"));
        });
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(source), config);
    }
}
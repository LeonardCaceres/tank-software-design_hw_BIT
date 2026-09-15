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
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.graphics.TankRenderer;
import ru.mipt.bit.platformer.graphics.TreeRenderer;
import ru.mipt.bit.platformer.input.GdxInputSource;
import ru.mipt.bit.platformer.input.InputHandlerRegistry;
import ru.mipt.bit.platformer.input.InputSource;
import ru.mipt.bit.platformer.input.TankMovementHandler;
import ru.mipt.bit.platformer.model.Field;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

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

        field = new Field();
        Tree tree = new Tree(new GridPoint2(1, 3));
        field.addTree(tree);
        tank = new Tank(new GridPoint2(1, 1));

        TankRenderer tankRenderer = new TankRenderer(
                new TextureRegion(blueTankTexture),
                levelRenderer.getTileMovement());
        TreeRenderer treeRenderer = new TreeRenderer(
                new TextureRegion(greenTreeTexture),
                levelRenderer.getGroundLayer(),
                tree.getCoordinates());
        renderer = new GameRenderer(levelRenderer, tankRenderer, treeRenderer);

        InputSource input = new GdxInputSource();
        inputHandlers = new InputHandlerRegistry();
        inputHandlers.register(new TankMovementHandler(input, tank, field));
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

    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }

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
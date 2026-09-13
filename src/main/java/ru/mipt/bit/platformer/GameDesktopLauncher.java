package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;
import java.util.ArrayList;
import java.util.List;

public class GameDesktopLauncher implements ApplicationListener {

//    private static final float MOVEMENT_SPEED = 0.4f; в танк
//
//    private Batch batch;
//
//    private TiledMap level; в филд
//    private MapRenderer levelRenderer;
//    private TileMovement tileMovement;
//
//    private Texture blueTankTexture;
//    private TextureRegion playerGraphics; в танк все что player*
//    private Rectangle playerRectangle;
//    // player current position coordinates on level 10x8 grid (e.g. x=0, y=1)
//    private GridPoint2 playerCoordinates;
//    // which tile the player want to go next
//    private GridPoint2 playerDestinationCoordinates;
//    private float playerMovementProgress = 1f;
//    private float playerRotation;
//
//    private Texture greenTreeTexture;
//    private TextureRegion treeObstacleGraphics; в tree
//    private GridPoint2 treeObstacleCoordinates = new GridPoint2();
//    private Rectangle treeObstacleRectangle = new Rectangle();
    private Batch batch;
    private Field field;
    private Tank tank;
    private Texture blueTankTexture;
    private Texture greenTreeTexture;
    @Override
    public void create() {
        batch = new SpriteBatch();
//
//        // load level tiles
//        level = new TmxMapLoader().load("level.tmx");
//        levelRenderer = createSingleLayerMapRenderer(level, batch);
//        TiledMapTileLayer groundLayer = getSingleLayer(level);
//        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
//
//        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
//        blueTankTexture = new Texture("images/tank_blue.png");
//        // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
//        playerGraphics = new TextureRegion(blueTankTexture);
//        playerRectangle = createBoundingRectangle(playerGraphics);
//        // set player initial position
//        playerDestinationCoordinates = new GridPoint2(1, 1);
//        playerCoordinates = new GridPoint2(playerDestinationCoordinates);
//        playerRotation = 0f;
//
//        greenTreeTexture = new Texture("images/greenTree.png");
//        treeObstacleGraphics = new TextureRegion(greenTreeTexture);
//        treeObstacleCoordinates = new GridPoint2(1, 3);
//        treeObstacleRectangle = createBoundingRectangle(treeObstacleGraphics);
//        moveRectangleAtTileCenter(groundLayer, treeObstacleRectangle, treeObstacleCoordinates)
//        меньше инициализируем теперь

//        сделали инициализацию в одном 9=(отдельном) классе:
        TiledMap level = new TmxMapLoader().load("level.tmx");
        field = new Field(level, batch);

        blueTankTexture = new Texture("images/tank_blue.png");
        greenTreeTexture = new Texture("images/greenTree.png");

        tank = new Tank(new TextureRegion(blueTankTexture),
                new GridPoint2(1, 1),
                field.getTileMovement());

        Tree tree = new Tree(new TextureRegion(greenTreeTexture),
                new GridPoint2(1, 3),
                field.getGroundLayer());
        field.addTree(tree);
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();
// жесткое повторение кода
//        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
//            if (isEqual(playerMovementProgress, 1f)) {
//                // check potential player destination for collision with obstacles
//                if (!treeObstacleCoordinates.equals(incrementedY(playerCoordinates))) {
//                    playerDestinationCoordinates.y++;
//                    playerMovementProgress = 0f;
//                }
//                playerRotation = 90f;
//            }
//        }
//        if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
//            if (isEqual(playerMovementProgress, 1f)) {
//                if (!treeObstacleCoordinates.equals(decrementedX(playerCoordinates))) {
//                    playerDestinationCoordinates.x--;
//                    playerMovementProgress = 0f;
//                }
//                playerRotation = -180f;
//            }
//        }
//        if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
//            if (isEqual(playerMovementProgress, 1f)) {
//                if (!treeObstacleCoordinates.equals(decrementedY(playerCoordinates))) {
//                    playerDestinationCoordinates.y--;
//                    playerMovementProgress = 0f;
//                }
//                playerRotation = -90f;
//            }
//        }
//        if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
//            if (isEqual(playerMovementProgress, 1f)) {
//                if (!treeObstacleCoordinates.equals(incrementedX(playerCoordinates))) {
//                    playerDestinationCoordinates.x++;
//                    playerMovementProgress = 0f;
//                }
//                playerRotation = 0f;
//            }
//        }
//      вместо треша выше - все в отдельной функции
        Direction direction = getPressedDirection();
        if (direction != null) {
            tank.move(direction, field);
        }



        // calculate interpolated player screen coordinates
//        tileMovement.moveRectangleBetweenTileCenters(playerRectangle, playerCoordinates, playerDestinationCoordinates, playerMovementProgress);
//
//        playerMovementProgress = continueProgress(playerMovementProgress, deltaTime, MOVEMENT_SPEED);
//        if (isEqual(playerMovementProgress, 1f)) {
//            // record that the player has reached his/her destination
//            playerCoordinates.set(playerDestinationCoordinates);
//        }
//      сократил обновление до 1 строчки (все теперь в танке)
        tank.update(deltaTime);

        // render each tile of the level
//        levelRenderer.render();
//
//        // start recording all drawing commands
//        batch.begin();
//
//        // render player
//        drawTextureRegionUnscaled(batch, playerGraphics, playerRectangle, playerRotation);
//
//        // render tree obstacle
//        drawTextureRegionUnscaled(batch, treeObstacleGraphics, treeObstacleRectangle, 0f);
//
//        // submit all drawing requests
//        batch.end();
//      по факту теперь все вызываем внутри методов:
        field.renderLevel();

        batch.begin();
        tank.render(batch);
        field.renderTrees(batch);
        batch.end();
    }
//    функция которой щаменил if-ы
    private Direction getPressedDirection() {
        if (Gdx.input.isKeyPressed(UP)    || Gdx.input.isKeyPressed(W)) return Direction.UP;
        if (Gdx.input.isKeyPressed(DOWN)  || Gdx.input.isKeyPressed(S)) return Direction.DOWN;
        if (Gdx.input.isKeyPressed(LEFT)  || Gdx.input.isKeyPressed(A)) return Direction.LEFT;
        if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) return Direction.RIGHT;
        return null;
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
//      level вшит в field
        field.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}

// Новые класс(добавленные)
enum Direction {
    UP   ( 0,  1,  90f),
    DOWN ( 0, -1, -90f),
    LEFT (-1,  0, -180f),
    RIGHT( 1,  0,   0f);

    private final int dx;
    private final int dy;
    private final float rotation;

    Direction(int dx, int dy, float rotation) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
    }

    GridPoint2 next(GridPoint2 from) {
        return new GridPoint2(from.x + dx, from.y + dy);
    }

    float getRotation() {
        return rotation;
    }
}

class Tree {
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;

    Tree(TextureRegion graphics, GridPoint2 coordinates, TiledMapTileLayer groundLayer) {
        this.graphics = graphics;
        this.coordinates = new GridPoint2(coordinates);
        this.rectangle = createBoundingRectangle(graphics);
        moveRectangleAtTileCenter(groundLayer, rectangle, coordinates);
    }

    GridPoint2 getCoordinates() {
        return coordinates;
    }

    void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }
}

class Tank {
    private static final float MOVEMENT_SPEED = 0.4f;

    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final TileMovement tileMovement;

    private float movementProgress = 1f;
    private float rotation = 0f;

    Tank(TextureRegion graphics, GridPoint2 start, TileMovement tileMovement) {
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
        this.coordinates = new GridPoint2(start);
        this.destinationCoordinates = new GridPoint2(start);
        this.tileMovement = tileMovement;
    }

    void move(Direction direction, Field field) {
        if (isEqual(movementProgress, 1f)) {
            GridPoint2 next = direction.next(coordinates);
            if (!field.isObstacle(next)) {
                destinationCoordinates.set(next);
                movementProgress = 0f;
            }
        }
        rotation = direction.getRotation();
    }

    void update(float deltaTime) {
        tileMovement.moveRectangleBetweenTileCenters(
                rectangle, coordinates, destinationCoordinates, movementProgress);

        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);

        if (isEqual(movementProgress, 1f)) {
            coordinates.set(destinationCoordinates);
        }
    }

    void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, rotation);
    }
}

class Field {
    private final TiledMap level;
    private final MapRenderer levelRenderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;
    private final List<Tree> trees = new ArrayList<>();

    Field(TiledMap level, Batch batch) {
        this.level = level;
        this.levelRenderer = createSingleLayerMapRenderer(level, batch);
        this.groundLayer = getSingleLayer(level);
        this.tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    TileMovement getTileMovement() {
        return tileMovement;
    }

    void addTree(Tree tree) {
        trees.add(tree);
    }

    boolean isObstacle(GridPoint2 coordinates) {
        for (Tree tree : trees) {
            if (tree.getCoordinates().equals(coordinates)) {
                return true;
            }
        }
        return false;
    }

    void renderLevel() {
        levelRenderer.render();
    }

    void renderTrees(Batch batch) {
        for (Tree tree : trees) {
            tree.render(batch);
        }
    }

    void dispose() {
        level.dispose();
    }
}
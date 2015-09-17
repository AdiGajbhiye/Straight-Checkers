package com.pennywise.findwords.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pennywise.FindWords;
import com.pennywise.findwords.core.Constants;
import com.pennywise.findwords.core.GameCam;
import com.pennywise.findwords.objects.Tile;

/**
 * Created by Joshua.Nabongo on 4/15/2015.
 */
public class GameScreen extends AbstractScreen {


    private final Stage stage;
    private final GameCam camera;
    private ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    private final BitmapFont font;
    private Vector2[] dragTracker;
    private float cellsize = 0;

    public GameScreen(FindWords game) {
        super(game);
        camera = GameCam.instance;
        stage = new Stage(new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera));
        Gdx.input.setInputProcessor(new InputMultiplexer(stage));
        font = loadFont("fonts/kenvector_future.ttf");
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        dragTracker = new Vector2[2];
        dragTracker[0] = new Vector2();
        buildBoard();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        stage.act();
        stage.draw();

        // shapeRenderer.begin();
        if (dragTracker[1] != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            drawLineForSelected(shapeRenderer);
            //Gdx.gl.glDisable(GL20.GL_BLEND);
            // shapeRenderer.end();
        }

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
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    //fonts/Roboto-Regular.ttf
    public BitmapFont loadFont(String path) {
        // how much bigger is the real device screen, compared to the defined viewport
        float SCALE = 1.0f * Gdx.graphics.getWidth() / Constants.GAME_WIDTH;
        // prevents unwanted downscale on devices with resolution SMALLER than 320x480
        if (SCALE < 1)
            SCALE = 1;
        //set the font parameters
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (14 * SCALE);
        parameter.flip = true;
        parameter.color = Color.BLACK;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        // 12 is the size i want to give for the font on all devices
        // bigger font textures = better results
        BitmapFont font = generator.generateFont(parameter);
        // aplly the inverse scale of what Libgdx will do at runtime
        font.getData().setScale((float) (1.0 / SCALE));
        // the resulting font scale is: 1.0 / SCALE * SCALE = 1
        //Apply Linear filtering; best choice to keep everything looking sharp
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return font;
    }


    private void buildBoard() {
        // build all layers

        Table layerGrid = buildGrid();

        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        stack.add(layerGrid);
    }

    private Table buildGrid() {
        Table layer = new Table();
        layer.addActor(grid(8, 8));
        return layer;
    }

    private Group grid(int rows, int cols) {

        Group grid = new Group();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        style.background = tileTexture("background/beige_tile.png");

        Vector2[] position = new Vector2[rows * cols];

        Tile[] tiles = new Tile[rows * cols];

        cellsize = ((Constants.GAME_WIDTH - cols) / (cols));
        float padding = (Constants.GAME_WIDTH - (cellsize * cols)) / 2;
        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                index = col + (row * cols);
                position[index] = new Vector2((col * cellsize) + padding,
                        ((row * (cellsize)) + (Constants.GAME_HEIGHT * 0.20f)));
                tiles[index] = new Tile("A", new Label.LabelStyle(style));
                tiles[index].setSize(cellsize, cellsize);
                tiles[index].setAlignment(Align.center);
                tiles[index].setPosition(position[index].x, position[index].y);
                tiles[index].addListener(new DragProcessor());
                grid.addActor(tiles[index]);
            }
        }

        return grid;
    }

    public SpriteDrawable tileTexture(String name) {
        final Texture t = new Texture(Gdx.files.internal(name));
        Sprite sprite = new Sprite(t);
        SpriteDrawable drawable = new SpriteDrawable(sprite);
        return drawable;
    }


    private void drawLineForSelected(ShapeRenderer sr) {


        int startX = (int) ((dragTracker[0].x + cellsize) / 2);
        int startY = (int) ((dragTracker[0].y + cellsize) / 2);
        int endX = (int) dragTracker[1].x;
        int endY = (int) dragTracker[1].y;
        int size = (int) (cellsize - 2);


        sr.setColor(new Color().set(255.0f, 108.0f, 0.0f, 0.5f));
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        sr.rectLine(startX, startY, endX, endY, size);
        sr.arc(startX, startY, (size / 2), 0,180);
        sr.arc(endX, endY, (size / 2), 0, 180);
        shapeRenderer.end();


    }


    public class DragProcessor extends DragListener {

        @Override
        public void drag(InputEvent event, float x, float y, int pointer) {
            Actor actor = (Actor) event.getListenerActor();


            dragTracker[0].x = actor.getX();
            dragTracker[0].y = actor.getY();
            dragTracker[0] = actor.localToParentCoordinates(dragTracker[0]);

            dragTracker[1] = dragTracker[0].cpy();
            dragTracker[1].x = x;
            dragTracker[1].y = y;

            dragTracker[1] = actor.localToParentCoordinates(dragTracker[1]);

            System.out.println("Coord = " + x + "," + y);
            System.out.println("Delta = " + this.getDeltaX() + "," + this.getDeltaY());

            // actor.setX(actor.getX() - this.getDeltaX());
            // actor.setY(actor.getY() - this.getDeltaY());
        }
    }

}

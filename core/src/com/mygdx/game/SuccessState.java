package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class SuccessState implements Screen {

    MyGdxGame game; // Note it’s "MyGdxGame" not "Game"
    // constructor to keep a reference to the main Game class
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;
    private Texture texture;
    private Label label;



    public SuccessState(MyGdxGame game){
        this.game = game;
    }
    public void create() {


        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("背景图.png"));
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage(new StretchViewport(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT));



        label = new Label("YOU WIN!", skin);
        label.setFontScale(2);
        float labelX = (MyGdxGame.WORLD_WIDTH / 2) - label.getWidth();
        float labelY = MyGdxGame.WORLD_HEIGHT / 3 * 2 + label.getHeight() / 2;
        label.setPosition(labelX, labelY);
        stage.addActor(label);

        TextButton retryButton = new TextButton("RETRY", skin);
        retryButton.setWidth(150f);
        retryButton.setHeight(75f);
        retryButton.setPosition(Gdx.graphics.getWidth() /2 - 150f, Gdx.graphics.getHeight()/2 - 10f);
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new GameScreen(game));
            }
        });
        stage.addActor(retryButton);

        TextButton quitButton = new TextButton("QUIT", skin);
        quitButton.setWidth(150f);
        quitButton.setHeight(75f);
        quitButton.setPosition(Gdx.graphics.getWidth() /2 - 150f, Gdx.graphics.getHeight()/2 - 100f);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.exit();
            }
        });
        stage.addActor(quitButton);

        Gdx.input.setInputProcessor(stage);
    }









    public void render(float f) {


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texture,0,0);

        batch.end();
        stage.act();
        stage.draw();

    }
    @Override
    public void dispose() { }
    @Override
    public void resize(int width, int height) { }
    @Override
    public void pause() { }
    @Override
    public void resume() { }
    @Override
    public void show() {

        create();
    }
    @Override
    public void hide() {

    }
}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class OverScreen implements Screen {
    MyGdxGame game; // Note it’s "MyGdxGame" not "Game"
    // constructor to keep a reference to the main Game class
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;
    private Texture texture;
    private Label label;

    public OverScreen(MyGdxGame game){
        this.game = game;
    }
    public void create() {

        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("背景图.png"));
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        stage = new Stage(new StretchViewport(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT));



        label = new Label("Game Over!", skin);
        label.setFontScale(2);
        float labelX = (MyGdxGame.WORLD_WIDTH / 2) - label.getWidth();
        float labelY = MyGdxGame.WORLD_HEIGHT / 3 * 2 + label.getHeight() / 2;
        label.setPosition(labelX, labelY);
        stage.addActor(label);

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

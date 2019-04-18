package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen implements Screen {
    MyGdxGame game; // Note it’s "MyGdxGame" not "Game"
    // constructor to keep a reference to the main Game class

    Texture padTex;
    Texture ballTex;
    Texture brickTex;
    Sound hitSound;
    Texture texture;
    Stage stage;

    public GameScreen(MyGdxGame game){
        this.game = game;

        stage = new Stage(new StretchViewport(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT));
        padTex = new Texture("paddle.png");
        ballTex = new Texture("ball.png");
        brickTex = new Texture("blue.png");
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
        texture = new Texture(Gdx.files.internal("背景图.png"));
    }
    public void create() {



    }
    public void render(float f) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            game.setScreen(new PausedScreen(game));
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        Gdx.app.log("GameScreen: "," show called");
        create();
    }
    @Override
    public void hide() {



    }
}
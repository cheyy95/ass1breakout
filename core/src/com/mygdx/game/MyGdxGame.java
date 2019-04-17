package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	public static final float WORLD_WIDTH = 640;
	public static final float WORLD_HEIGHT = 480;

	SpriteBatch batch;
	BitmapFont font;
	Music music;

	@Override
	public void create() {
		music = Gdx.audio.newMusic(Gdx.files.internal("bgm.mp3"));
		music.setLooping(true);
		music.setVolume(0.7f);
		music.play();

		batch = new SpriteBatch();
		font = new BitmapFont();
		setScreen(new MenuScreen(this));
	}
}

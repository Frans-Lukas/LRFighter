package com.mygdx.lrgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LRGame extends ApplicationAdapter {
	private SpriteBatch batch;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		GameLoop.setUp();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		GameLoop.Input();
		GameLoop.Update();
		batch.begin();
		GameLoop.Render(batch);
		batch.end();
		GameLoop.doPhysicsStep(Gdx.graphics.getDeltaTime());
	}

	/**
	 * User input
	 * Update
	 * Render
	 */
	
	@Override
	public void dispose () {
		batch.dispose();
		GameLoop.dispose();
	}
}

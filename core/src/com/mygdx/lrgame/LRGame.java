package com.mygdx.lrgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class LRGame implements ApplicationListener {
	public static final int WORLD_WIDTH = 100;
	public static final int WORLD_HEIGHT = 100;

	private OrthographicCamera cam;
    private SpriteBatch batch;

    private float rotationSpeed;
	
	@Override
	public void create () {
	    rotationSpeed = 0.5f;

	    float w = Gdx.graphics.getWidth();
	    float h = Gdx.graphics.getHeight();

	    cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
	    cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
	    cam.update();

		batch = new SpriteBatch();
		GameLoop.setUp();
	}

    @Override
    public void resize(int width, int height) {
	    cam.viewportWidth = WORLD_WIDTH;
	    cam.viewportHeight = WORLD_HEIGHT * height / width;
	    cam.update();
    }

    @Override
	public void render () {
        GameLoop.Input();
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		GameLoop.Update();
		batch.begin();
		GameLoop.Render(batch, cam);
		batch.end();
		GameLoop.doPhysicsStep(Gdx.graphics.getDeltaTime());
	}

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

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

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

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom += 0.02;
            //If the A Key is pressed, add 0.02 to the Camera's Zoom
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= 0.02;
            //If the Q Key is pressed, subtract 0.02 from the Camera's Zoom
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-3, 0, 0);
            //If the LEFT Key is pressed, translate the camera -3 units in the X-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(3, 0, 0);
            //If the RIGHT Key is pressed, translate the camera 3 units in the X-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.translate(0, -3, 0);
            //If the DOWN Key is pressed, translate the camera -3 units in the Y-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.translate(0, 3, 0);
            //If the UP Key is pressed, translate the camera 3 units in the Y-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.rotate(-rotationSpeed, 0, 0, 1);
            //If the W Key is pressed, rotate the camera by -rotationSpeed around the Z-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.rotate(rotationSpeed, 0, 0, 1);
            //If the E Key is pressed, rotate the camera by rotationSpeed around the Z-Axis
        }

        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 100/cam.viewportWidth);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
    }

    @Override
	public void render () {
        GameLoop.Input();
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		GameLoop.Update();
		batch.begin();
		GameLoop.Render(batch);
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

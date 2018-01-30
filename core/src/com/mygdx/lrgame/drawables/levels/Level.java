package com.mygdx.lrgame.drawables.levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.mygdx.lrgame.LRGame.WORLD_HEIGHT;
import static com.mygdx.lrgame.LRGame.WORLD_WIDTH;

public class Level {
    private Sprite background;
    private Sprite backgroundLeft;
    private Sprite backgroundRight;

    public Level() {
        background = new Sprite(new Texture("Background.png"));
        background.setPosition(0,0);
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);


        backgroundLeft = new Sprite(new Texture("Background.png"));
        backgroundLeft.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        backgroundLeft.setPosition(-WORLD_WIDTH,0);

        backgroundRight = new Sprite(new Texture("Background.png"));
        backgroundRight.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        backgroundRight.setPosition(WORLD_WIDTH,0);



    }

    public Sprite getBackground() {
        return background;
    }

    public void update(){

    }

    public void render(SpriteBatch batch){
        background.draw(batch);
        backgroundLeft.draw(batch);
        backgroundRight.draw(batch);

    }

    public void move(float xSpeed, float ySpeed) {
        background.setPosition(background.getX() + xSpeed, background.getY() + ySpeed);
        backgroundLeft.setPosition(backgroundLeft.getX() + xSpeed, backgroundLeft.getY() + ySpeed);
        backgroundRight.setPosition(backgroundRight.getX() + xSpeed, backgroundRight.getY() + ySpeed);
    }
}

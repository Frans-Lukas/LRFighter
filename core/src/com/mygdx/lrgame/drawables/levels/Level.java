package com.mygdx.lrgame.drawables.levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import static com.mygdx.lrgame.LRGame.WORLD_HEIGHT;
import static com.mygdx.lrgame.LRGame.WORLD_WIDTH;

public class Level {
    private Sprite background;

    public Level() {
        background = new Sprite(new Texture("Background.png"));
        background.setPosition(0,0);
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
    }

    public Sprite getBackground() {
        return background;
    }

    public void update(){

    }

    public void move(float xSpeed, float ySpeed) {
    }
}

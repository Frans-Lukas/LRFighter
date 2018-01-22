package com.mygdx.lrgame.drawables.levels;

import com.badlogic.gdx.graphics.Texture;

public class Level {
    private Texture background;

    public Level() {
        background = new Texture("Background.png");
    }

    public Texture getBackground() {
        return background;
    }

    public void update(){

    }
}

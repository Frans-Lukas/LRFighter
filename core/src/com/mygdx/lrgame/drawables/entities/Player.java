package com.mygdx.lrgame.drawables.entities;

import java.util.ArrayList;

public class Player extends Entity {
    private final int range = 200;

    public Player(int health, int x, int y) {
        super(health, x, y);
    }

    @Override
    public void takeDamage() {

    }

    public void update(ArrayList<Enemy> entities) {

    }

    public int getRange() {
        return range;
    }
}

package com.mygdx.lrgame.drawables.entities;

import com.mygdx.lrgame.other.Position;

public class Entity {
    protected int health = 1;
    protected int x;
    protected int y;


    public Entity(int health, int x, int y) {
        this.health = health;
        this.x = x;
        this.y = y;
    }

    /**
     * Deal damage to this entity.
     * i.e. reduce health.
     */
    public void takeDamage(){

    }

    public int getHealth() {
        return health;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}

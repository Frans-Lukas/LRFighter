package com.mygdx.lrgame.drawables.entities;

import com.mygdx.lrgame.other.Position;

public class Entity {
    protected int health = 1;
    protected Position pos = null;

    public Entity(int health, Position pos) {
        this.health = health;
        this.pos = pos;
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

    public Position getPos() {
        return pos;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}

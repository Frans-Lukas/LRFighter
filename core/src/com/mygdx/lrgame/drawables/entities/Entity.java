package com.mygdx.lrgame.drawables.entities;

import com.mygdx.lrgame.other.Position;

public abstract class Entity {
    protected int health;
    protected Position pos;

    public Entity(int health, Position pos) {
        this.health = health;
        this.pos = pos;
    }

    /**
     * Deal damage to this entity.
     * i.e. reduce health.
     */
    public void takeDamage(){ }
}

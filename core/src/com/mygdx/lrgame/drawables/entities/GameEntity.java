package com.mygdx.lrgame.drawables.entities;

import com.badlogic.gdx.Gdx;

public class GameEntity {
    protected float x;
    protected float y;
    protected int health = 1;
    protected float xSpeed = 0;
    protected float ySpeed = 1;
    private static int WIDTH = 2;
    private static int HEIGHT = 2;
    protected EntityState currentEntityState;


    public GameEntity(int health, float x, float y) {
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static int getWidth() {
        return WIDTH;
    }

    public EntityState getCurrentEntityState() {
        return currentEntityState;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public float getXSpeed() {
        return (xSpeed)* Gdx.graphics.getDeltaTime() * 100 ;
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

    public void setCurrentEntityState(EntityState currentEntityState) {
        this.currentEntityState = currentEntityState;
    }

    public enum EntityState {
        STATE_ATTACKING,
        STATE_DYING,
        STATE_MOVING,
        STATE_FLYING,
        STATE_READY
    }
}

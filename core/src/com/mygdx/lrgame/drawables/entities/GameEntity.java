package com.mygdx.lrgame.drawables.entities;

import com.badlogic.gdx.Gdx;

public class GameEntity {
    protected int x;
    protected int y;
    protected int health = 1;
    protected int xSpeed = 1;
    protected int ySpeed = 4;
    private static int WIDTH = 32;
    private static int HEIGHT = 32;
    protected EntityState currentEntityState;


    public GameEntity(int health, int x, int y) {
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

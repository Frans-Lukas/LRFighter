package com.mygdx.lrgame.drawables.entities;

import java.util.ArrayList;

public class Player extends Entity {
    private final int range = 200;
    private Enemy closestEnemy;
    private EntityState currentEntityState;

    public Player(int health, int x, int y) {
        super(health, x, y);
        currentEntityState = EntityState.STATE_READY;
    }

    @Override
    public void takeDamage() {

    }

    public void update(ArrayList<Enemy> entities, boolean leftIsPressed, boolean rightIsPressed) {
    }

    private boolean enemyIsInRange(ArrayList<Enemy> entities) {
        int enemyDistance = 5000;
        for (Enemy entity : entities) {
            int tempDistance = Math.abs(entity.x - x);
            if(tempDistance <  enemyDistance && tempDistance < range){
                closestEnemy = entity;
                enemyDistance =  tempDistance;
            }
        }


        return true;
    }

    public int getRange() {
        return range;
    }

    @Override
    public EntityState getCurrentEntityState() {
        return currentEntityState;
    }
}

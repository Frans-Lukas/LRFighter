package com.mygdx.lrgame.drawables.entities;

import java.util.ArrayList;

public class Player extends Entity {
    private final int range = 200;
    private Enemy closestEnemy;

    public Player(int health, int x, int y) {
        super(health, x, y);
    }

    @Override
    public void takeDamage() {

    }

    public void update(ArrayList<Enemy> entities, boolean leftIsPressed, boolean rightIsPressed) {
        if(leftIsPressed && enemyIsInRange(entities)){
            if(closestEnemy != null){
                closestEnemy.x = x - Entity.getWidth();
            }
        }
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
}

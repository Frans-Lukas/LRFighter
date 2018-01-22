package com.mygdx.lrgame.drawables.entities.player;

import com.mygdx.lrgame.drawables.entities.Enemy;
import com.mygdx.lrgame.drawables.entities.Entity;

import java.util.ArrayList;

public class Player extends Entity {
    private final int range = 200;
    private Enemy closestEnemy;
    private EntityState currentEntityState;
    private PlayerState state;

    public Player(int health, int x, int y, PlayerState state) {
        super(health, x, y);
        currentEntityState = EntityState.STATE_READY;
        this.state = state;
    }

    @Override
    public void takeDamage() {

    }

    public void update() {
        state.update();
    }

    private boolean enemyIsInRange(ArrayList<Enemy> entities) {
        int enemyDistance = 5000;
        for (Enemy entity : entities) {
            int tempDistance = Math.abs(entity.getX() - x);
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

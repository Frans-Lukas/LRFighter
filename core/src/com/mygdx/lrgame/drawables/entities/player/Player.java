package com.mygdx.lrgame.drawables.entities.player;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.lrgame.drawables.entities.enemy.Enemy;
import com.mygdx.lrgame.drawables.entities.GameEntity;

public class Player extends GameEntity {
    private final int range = 15;
    private Enemy closestEnemy;

    public Player(int health, int x, int y) {
        super(health, x, y);
        currentEntityState = EntityState.STATE_READY;
        xSpeed = 1;
    }

    @Override
    public void takeDamage() {

    }

    public void update() {
    }

    public int getRange() {
        return range;
    }

    public Rectangle getPlayerRect(){
        return new Rectangle(x, y, getWidth(), getHeight());
    }
}

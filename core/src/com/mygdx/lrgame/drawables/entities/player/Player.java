package com.mygdx.lrgame.drawables.entities.player;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.lrgame.drawables.entities.enemy.Enemy;
import com.mygdx.lrgame.drawables.entities.GameEntity;

public class Player extends GameEntity {
    private final int range = 200;
    private Enemy closestEnemy;
    private EntityState currentEntityState;
    private PlayerState state;

    public Player(int health, int x, int y, PlayerState state) {
        super(health, x, y);
        currentEntityState = EntityState.STATE_READY;
        this.state = state;
        xSpeed = 10;
    }

    @Override
    public void takeDamage() {

    }

    public void update() {
        state.update();
    }

    public int getRange() {
        return range;
    }

    @Override
    public EntityState getCurrentEntityState() {
        return currentEntityState;
    }

    public Rectangle getPlayerRect(){
        return new Rectangle(x, y, getWidth(), getHeight());
    }
}

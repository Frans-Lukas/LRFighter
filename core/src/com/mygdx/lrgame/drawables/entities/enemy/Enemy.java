package com.mygdx.lrgame.drawables.entities.enemy;

import com.badlogic.gdx.Gdx;
import com.mygdx.lrgame.GameLoop;
import com.mygdx.lrgame.drawables.entities.GameEntity;
import com.mygdx.lrgame.drawables.entities.player.Player;

public class Enemy extends GameEntity {

    public Enemy(int health, float x, float y) {
        super(health, x, y);
        xSpeed = 0.3f;
    }

    @Override
    public void takeDamage() {
        health--;
    }

    /**
     * Move towards player.
     * @param player the player to move to
     * @return whether the enemy reached the player or not
     */
    public void update(Player player) {

        //This enemy is to the right of the player
        if (x > player.getX() + player.getWidth()) {
            if (x - getXSpeed() < player.getX() + player.getWidth()) {
                x = player.getX() + player.getWidth();
            } else {
                x -= getXSpeed();
            }
        } //this enemy is to the left of the enemy.
        else if (x < player.getX() - player.getWidth()) {
            if (x + getXSpeed() > player.getX() - player.getWidth()) {
                x = player.getX() - player.getWidth();
            } else {
                x += getXSpeed();
            }
        }
    }




}

package com.mygdx.lrgame.drawables.entities.enemy;

import com.mygdx.lrgame.drawables.entities.GameEntity;
import com.mygdx.lrgame.drawables.entities.player.Player;

public class Enemy extends GameEntity {



    public Enemy(int health, int x, int y) {
        super(health, x, y);
        xSpeed = 3;
    }

    @Override
    public void takeDamage() {

    }

    /**
     * Move towards player.
     * @param player the player to move to
     * @param extraSpeed
     */
    public void update(Player player, float extraSpeed) {
        //This enemy is to the right of the player
        if(x > player.getX() + player.getWidth()){
            if(x - getXSpeed(extraSpeed) < player.getX() + player.getWidth()){
                x = player.getX() + player.getWidth();
            } else{
                x -= getXSpeed(extraSpeed);
            }
        } //this enemy is to the left of the enemy.
        else if(x < player.getX() - player.getWidth()){
            if(x + getXSpeed(extraSpeed) > player.getX() - player.getWidth()){
                x = player.getX() - player.getWidth();
            } else{
                x += getXSpeed(extraSpeed);
            }
        }
    }


}

package com.mygdx.lrgame.drawables.entities;

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
     */
    public void update(Player player) {
        int dx = player.x - x;
        if(dx < -GameEntity.getWidth()){
            if(x - xSpeed < player.x + GameEntity.getWidth()){
                x = player.x + GameEntity.getWidth();
            }
            else{
                x -= getXSpeed();
            }
        } else if(dx > GameEntity.getWidth()){
            if(dx < GameEntity.getWidth() - xSpeed)
                x = player.x + GameEntity.getWidth();
            else
                x += getXSpeed();
        }
    }
}

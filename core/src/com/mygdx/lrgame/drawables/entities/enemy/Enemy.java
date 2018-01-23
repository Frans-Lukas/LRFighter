package com.mygdx.lrgame.drawables.entities.enemy;

import com.badlogic.gdx.Gdx;
import com.mygdx.lrgame.GameLoop;
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
    public boolean update(Player player, float extraSpeed) {
        if(extraSpeed == 0) {
            //This enemy is to the right of the player
            if (x > player.getX() + player.getWidth()) {
                if (x - getXSpeed() < player.getX() + player.getWidth()) {
                    x = player.getX() + player.getWidth();
                    return true;
                } else {
                    x -= getXSpeed();
                }
            } //this enemy is to the left of the enemy.
            else if (x < player.getX() - player.getWidth()) {
                if (x + getXSpeed() > player.getX() - player.getWidth()) {
                    x = player.getX() - player.getWidth();
                    return true;
                } else {
                    x += getXSpeed();
                }
            }
        } else{
            float distanceToPlayer = Math.abs(getX() - player.getX());
            float distanceToTravel = extraSpeed * 100 * Gdx.graphics.getDeltaTime();
            if(distanceToPlayer <= Math.abs(distanceToTravel)){
                if(GameLoop.isToTheLeft(player, this)){
                    x = player.getX() - player.getWidth();
                } else if(GameLoop.isToTheRight(player, this)){
                    x = player.getX() + player.getWidth();
                }
                return true;
            } else{
                x += distanceToTravel;
            }
        }
        return false;
    }


}

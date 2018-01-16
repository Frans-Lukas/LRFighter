package com.mygdx.lrgame.drawables.entities;

public class Enemy extends Entity {

    public Enemy(int health, int x, int y) {
        super(health, x, y);
    }

    @Override
    public void takeDamage() {

    }

    /**
     * Move towards player.
     * @param player
     */
    public void update(Player player) {
        int dx = player.x - x;
        if(dx < Entity.getWidth()){
            if(dx < Entity.getWidth() + xSpeed){
                x = player.x - Entity.getWidth();
            } else
                x -= xSpeed;
        } else if(dx > Entity.getWidth()){
            if(dx > Entity.getWidth() - xSpeed)
                x = player.x + Entity.getWidth();
            else
                x += xSpeed;
        }
    }
}

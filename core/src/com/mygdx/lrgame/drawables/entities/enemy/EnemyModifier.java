package com.mygdx.lrgame.drawables.entities.enemy;

public class EnemyModifier {
    private int xSpeed;
    private int ySpeed;
    private boolean disableOnCollision = false;
    private boolean disableOnDeath = false;
    private boolean disableOnHit = false;


    public EnemyModifier(int xSpeed, int ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }
}

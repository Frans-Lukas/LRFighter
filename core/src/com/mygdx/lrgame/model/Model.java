package com.mygdx.lrgame.model;

import com.badlogic.gdx.Gdx;
import com.mygdx.lrgame.drawables.entities.GameEntity;
import com.mygdx.lrgame.drawables.entities.enemy.Enemy;
import com.mygdx.lrgame.drawables.entities.enemy.EnemyRagdoll;
import com.mygdx.lrgame.drawables.entities.player.Player;

import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.lrgame.GameLoop.GAME_HEIGHT;
import static com.mygdx.lrgame.GameLoop.GAME_WIDTH;

public class Model {


    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    public static final int X_FORCE_VARIANCE = 400;
    public static final int Y_FORCE_VARIANCE = 100;
    private static final float PLAYER_X_FORCE = 650f;
    private static final float PLAYER_Y_FORCE = 200f;
    private static final float SPAWN_TIMER = 1;

    private static float actualSpawnTimer = 1;
    private static float currentTime = 0;
    private static boolean spawnAtLeftSide;

    private Player player;
    private ArrayList<Enemy> enemies;

    private static ArrayList<EnemyRagdoll> ragdolls;

    private static Random randGenerator;


    public void update(Player player, ArrayList<Enemy> enemies) {
        this.player = player;
        this.enemies = enemies;
    }


    public void updateLevel() {
        currentTime += Gdx.graphics.getDeltaTime();
        if(currentTime >= actualSpawnTimer){
            currentTime = 0;
            float x = -Enemy.getWidth();
            float y = GAME_HEIGHT / 2;
            if(!spawnAtLeftSide){
                x = GAME_WIDTH + Enemy.getWidth();
            }
            spawnAtLeftSide = !spawnAtLeftSide;

            actualSpawnTimer = SPAWN_TIMER - randGenerator.nextFloat() * 0.5f;

            Enemy enemy = new Enemy(1, x, y);
            enemies.add(enemy);
        }
    }

    /**
     * Removes ragdolls that are outside of the screen.
     */
    private static void updateRagdolls() {
        for (int i = 0; i < ragdolls.size(); i++) {
            EnemyRagdoll ragdoll = ragdolls.get(i);
            float x = ragdoll.getRagdollBody().getPosition().x;
            ragdoll.update();
            if(x > GAME_WIDTH  + GameEntity.getWidth() || x < -GameEntity.getWidth()){
                ragdolls.remove(ragdoll);
                i--;
            }
        }
    }
}

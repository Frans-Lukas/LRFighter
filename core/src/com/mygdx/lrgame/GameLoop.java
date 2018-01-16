package com.mygdx.lrgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.lrgame.drawables.entities.Enemy;
import com.mygdx.lrgame.drawables.entities.Entity;
import com.mygdx.lrgame.drawables.entities.Player;
import com.mygdx.lrgame.drawables.entities.levels.Level;

import java.util.ArrayList;
import java.util.HashMap;

public class GameLoop {

    private static final int GAME_HEIGHT = 540;
    private static final int GAME_WIDTH = 960;
    private static final int PLAYER_HEALTH = 3;

    private static final int BASIC_ENEMY_HEALTH = 1;
    private static final int ENEMY_START_POS_Y = GAME_HEIGHT / 2;
    private static Player player;

    private static ArrayList<Enemy> entities;
    private static HashMap<Class, Texture> flyweightMap;

    private static Level level1;

    public static void setUp(){
        /**
         * Create the player and a pool of entities.
         * Create a flyweight hashmap that the pool of entities can
         * get their textures from.
         */
        entities = new ArrayList<Enemy>();
        flyweightMap = new HashMap<Class, Texture>();

        player = new Player(PLAYER_HEALTH, GAME_WIDTH / 2 - Entity.getWidth() / 2, GAME_HEIGHT / 2);

        flyweightMap.put(Player.class, new Texture("Player.png"));
        flyweightMap.put(Enemy.class, new Texture("Enemy.png"));
        level1 = new Level();

        for (int i = 0; i < 4; i++) {
            Enemy enemy = new Enemy(BASIC_ENEMY_HEALTH, 0, ENEMY_START_POS_Y);
            entities.add(enemy);
        }
    }

    public static void Update(){
        for (Enemy entity : entities) {
            entity.update(player);
        }
    }

    public static void Input(){

    }

    public static void Render(SpriteBatch batch){

        //Always draw level background first.
        batch.draw(level1.getBackground(),0,0);

        //Then Enemies.
        for (com.mygdx.lrgame.drawables.entities.Entity entity : entities) {
            batch.draw(flyweightMap.get(entity.getClass()), entity.getX(), entity.getY());
        }

        //Lastly player
        batch.draw(flyweightMap.get(player.getClass()), player.getX(), player.getY());
    }
}

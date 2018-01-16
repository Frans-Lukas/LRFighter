package com.mygdx.lrgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.lrgame.drawables.entities.Enemy;
import com.mygdx.lrgame.drawables.entities.FlyweightEntityImage;
import com.mygdx.lrgame.drawables.entities.Player;
import com.mygdx.lrgame.other.Position;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameLoop {

    private static final int GAME_HEIGHT = 512;
    private static final int GAME_WIDTH = 512;
    private static final int PLAYER_HEALTH = 3;

    private static final int BASIC_ENEMY_HEALTH = 1;
    private static final int ENEMY_START_POS_Y = GAME_HEIGHT / 2;
    private static Player player;

    private static ArrayList<Enemy> entities;
    private static HashMap<Class, Texture> flyweightMap;

    public static void setUp(){
        /**
         * Create the player and a pool of entities.
         * Create a flyweight hashmap that the pool of entities can
         * get their textures from.
         */
        entities = new ArrayList<Enemy>();
        flyweightMap = new HashMap<Class, Texture>();

        player = new Player(PLAYER_HEALTH, GAME_WIDTH / 2, GAME_HEIGHT / 2);

        flyweightMap.put(Player.class, new Texture("Player.png"));
        flyweightMap.put(Enemy.class, new Texture("Enemy.png"));

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
        batch.draw(flyweightMap.get(player.getClass()), player.getX(), player.getY());
        for (com.mygdx.lrgame.drawables.entities.Entity entity : entities) {
            batch.draw(flyweightMap.get(entity.getClass()), entity.getX(), entity.getY());
        }

    }
}

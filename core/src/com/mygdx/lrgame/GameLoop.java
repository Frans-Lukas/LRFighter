package com.mygdx.lrgame;

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
    private static final Position ENEMY_START_POS = new Position(0, GAME_HEIGHT / 2);


    private List<com.mygdx.lrgame.drawables.entities.Entity> entities;
    private HashMap<Class, FlyweightEntityImage> flyweightMap;

    public GameLoop() {
        /**
         * Create the player and a pool of entities.
         * Create a flyweight hashmap that the pool of entities can
         * get their textures from.
         */
        entities = new ArrayList<com.mygdx.lrgame.drawables.entities.Entity>();
        flyweightMap = new HashMap<Class, FlyweightEntityImage>();

        Player player = new Player(PLAYER_HEALTH, new Position(GAME_WIDTH / 2, GAME_HEIGHT / 2));

        flyweightMap.put(player.getClass(), new FlyweightEntityImage());
        entities.add(player);

        boolean flyweightAdded = false;
        for (int i = 0; i < 4; i++) {
            Enemy enemy = new Enemy(BASIC_ENEMY_HEALTH, ENEMY_START_POS);
            entities.add(enemy);

            if(!flyweightAdded){
                flyweightMap.put(enemy.getClass(), new FlyweightEntityImage());
                flyweightAdded = true;
            }
        }

    }

    public static void Update(){

    }

    public static void Input(){

    }

    public static void Render(){

    }
}

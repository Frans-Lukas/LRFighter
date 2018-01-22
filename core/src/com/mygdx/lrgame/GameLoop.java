package com.mygdx.lrgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    private static Level level;

    private static boolean leftIsPressed;
    private static boolean rightIsPressed;

    private static ShapeRenderer shapeRenderer = new ShapeRenderer();
    private static Color rangeIndicatorLeftColor = Color.RED;
    private static Color rangeIndicatorRightColor = Color.BLUE;

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
        level = new Level();

        Enemy enemyLeft = new Enemy(BASIC_ENEMY_HEALTH, 0, ENEMY_START_POS_Y);
        Enemy enemyRight = new Enemy(BASIC_ENEMY_HEALTH, GAME_WIDTH, ENEMY_START_POS_Y);
        entities.add(enemyLeft);
        entities.add(enemyRight);

    }

    public static void Update(){
        level.update();
        updatePlayer();
        for (Enemy entity : entities) {
            entity.update(player);
        }

    }

    private static void updatePlayer() {
        switch(player.getCurrentEntityState()){
            case STATE_ATTACKING:
                break;
            case STATE_DYING:
                break;
            case STATE_MOVING:
                break;
            case STATE_FLYING:
                break;
            case STATE_READY:
                break;
            default:
                break;
        }
    }

    public static void Input(){
        leftIsPressed = Gdx.input.isKeyPressed(Input.Keys.A) ||
                        Gdx.input.isKeyPressed(Input.Keys.LEFT);
        rightIsPressed = Gdx.input.isKeyPressed(Input.Keys.D) ||
                        Gdx.input.isKeyPressed(Input.Keys.RIGHT);

    }

    public static void Render(SpriteBatch batch){

        //Always draw level background first.
        batch.draw(level.getBackground(),0,0);

        //Then range indicators
        batch.end();
        renderRange();
        batch.begin();

        //Then Enemies.
        for (com.mygdx.lrgame.drawables.entities.Entity entity : entities) {
            batch.draw(flyweightMap.get(entity.getClass()), entity.getX(), entity.getY(),
                    Entity.getWidth(),
                    Entity.getHeight());
        }

        //Lastly player
        batch.draw(flyweightMap.get(player.getClass()), player.getX(), player.getY(),
                Entity.getWidth(),
                Entity.getHeight());
    }

    private static void renderRange(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(rangeIndicatorRightColor);
        shapeRenderer.line(player.getX() + player.getRange(),
                player.getY(), player.getX() + player.getRange(),
                player.getY() - GAME_HEIGHT);

        shapeRenderer.setColor(rangeIndicatorLeftColor);
        shapeRenderer.line(player.getX() - player.getRange(),
                player.getY(), player.getX() - player.getRange(),
                player.getY() - GAME_HEIGHT);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}

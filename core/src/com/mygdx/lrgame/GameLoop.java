package com.mygdx.lrgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.lrgame.drawables.entities.Enemy;
import com.mygdx.lrgame.drawables.entities.GameEntity;
import com.mygdx.lrgame.drawables.levels.Level;
import com.mygdx.lrgame.drawables.entities.player.Player;
import com.mygdx.lrgame.drawables.entities.player.PlayerStateReady;

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

        player = new Player(PLAYER_HEALTH, GAME_WIDTH / 2 - GameEntity.getWidth() / 2, GAME_HEIGHT / 2, new PlayerStateReady());

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
                Enemy enemyToAttack = null;
                if(leftIsPressed){
                    enemyToAttack = findClosestEnemy(true);
                } else if(rightIsPressed){
                    enemyToAttack = findClosestEnemy(false);
                }
                if(enemyToAttack != null){
                    enemyToAttack.setX(0);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Find the closeset enemy either to the right or to the left of the player according
     * to the player range
     * @param toLeft if method should look to the left or right.
     * @return the enemy that is close, or null if no enemy is within range.
     */
    private static Enemy findClosestEnemy(final boolean toLeft) {
        //if toLeft get left half of screen else get right half of screen.
        final int range = toLeft ? -player.getRange() : player.getRange();

        //if toLeft get x pos of left side of player else get right x pos of player
        final int playerX = toLeft ? player.getX() : player.getX() + player.getWidth();

        //return closest enemy from the list.
        return entities.stream().
                filter(entity -> enemyIsInRange(playerX, range, entity.getX())).
                sorted((enemy1, enemy2) -> toLeft ? (enemy1.getX() - enemy2.getX()) : (enemy2.getX() - enemy1.getX())).
                findFirst().
                orElse(null);
    }

    private static boolean enemyIsInRange(int startPos, int width, int x) {
        int leftPoint = Math.min(startPos, startPos + width);
        int rightPoint = Math.max(startPos, startPos + width);
        return x >= leftPoint && x <= rightPoint;
    }

    public static void Input(){
        leftIsPressed = Gdx.input.isKeyPressed(Input.Keys.A) ||
                        Gdx.input.isKeyPressed(Input.Keys.LEFT);
        rightIsPressed = Gdx.input.isKeyPressed(Input.Keys.D) ||
                        Gdx.input.isKeyPressed(Input.Keys.RIGHT);

    }

    public static void Render(SpriteBatch batch){
        //Always draw level background first.
        renderBackground(batch);
        //Then range indicators
        renderRange(batch);
        //Then Enemies.
        renderEnemies(batch);
        //Lastly player
        renderPlayer(batch);
    }

    private static void renderPlayer(SpriteBatch batch) {
        batch.draw(flyweightMap.get(player.getClass()), player.getX(), player.getY(),
                GameEntity.getWidth(),
                GameEntity.getHeight());
    }

    private static void renderEnemies(SpriteBatch batch) {
        for (GameEntity entity : entities) {
            batch.draw(flyweightMap.get(entity.getClass()), entity.getX(), entity.getY(),
                    GameEntity.getWidth(),
                    GameEntity.getHeight());
        }
    }

    private static void renderBackground(SpriteBatch batch) {
        batch.draw(level.getBackground(),0,0);
    }

    /**
     * Render the range indicators for where the player can reach.
     * @param batch
     */
    private static void renderRange(SpriteBatch batch){
        batch.end();

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

        batch.begin();
    }

    /**
     * Disposes everything that needs disposing of in the main game loop.
     * Except the spriteBatch
     */
    public static void dispose() {
        for (Class textureClasses : flyweightMap.keySet()) {
            flyweightMap.get(textureClasses).dispose();
        }
        shapeRenderer.dispose();
    }
}

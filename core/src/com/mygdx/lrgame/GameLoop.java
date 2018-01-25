package com.mygdx.lrgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.lrgame.drawables.entities.enemy.EnemRagdoll;
import com.mygdx.lrgame.drawables.entities.enemy.Enemy;
import com.mygdx.lrgame.drawables.entities.GameEntity;
import com.mygdx.lrgame.drawables.entities.enemy.EnemyModifier;
import com.mygdx.lrgame.drawables.levels.Level;
import com.mygdx.lrgame.drawables.entities.player.Player;
import com.mygdx.lrgame.drawables.entities.player.PlayerStateReady;

import java.util.ArrayList;
import java.util.HashMap;

public class GameLoop {

    private static final int GAME_HEIGHT = 100;
    private static final int GAME_WIDTH = 100;
    private static final int PLAYER_HEALTH = 3;

    private static final int BASIC_ENEMY_HEALTH = 1;
    private static final int ENEMY_START_POS_Y = GAME_HEIGHT / 2;
    private static final float TIME_STEP = 1/45f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    private static Player player;
    private static Enemy enemyToAttack;

    private static ArrayList<Enemy> enemies;
    private static EnemyModifier nillModifier;
    private static EnemyModifier leftModifier;
    private static EnemyModifier rightModifier;
    private static HashMap<Class, Sprite> flyweightMap;

    private static ArrayList<EnemRagdoll> ragdolls;
    private static World physicsWorld;
    private static Box2DDebugRenderer debugRenderer;
    private static float accumulator;

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
        enemies = new ArrayList<Enemy>();
        ragdolls = new ArrayList<>();
        flyweightMap = new HashMap<Class, Sprite>();

        player = new Player(PLAYER_HEALTH, GAME_WIDTH / 2, GAME_HEIGHT / 2);
        enemyToAttack = null;

        physicsWorld = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        accumulator = 0;

        Sprite playerSprite = new Sprite(new Texture("Player.png"));
        playerSprite.setSize(player.getWidth(), player.getHeight());

        Sprite enemySprite = new Sprite(new Texture("Enemy.png"));
        enemySprite.setSize(player.getWidth(), player.getHeight());

        flyweightMap.put(Player.class, playerSprite);
        flyweightMap.put(Enemy.class, enemySprite);
        level = new Level();

        Enemy enemyLeft = new Enemy(BASIC_ENEMY_HEALTH, 0, ENEMY_START_POS_Y);
        Enemy enemyRight = new Enemy(BASIC_ENEMY_HEALTH, GAME_WIDTH, ENEMY_START_POS_Y);
        enemies.add(enemyLeft);
        enemies.add(enemyRight);

    }

    public static void Update(){
        level.update();
        updatePlayer();
        for (Enemy enemy : enemies) {
            enemy.update(player);
        }
    }

    /**
     * Update player according to state
     */
    private static void updatePlayer() {

        switch(player.getCurrentEntityState()){
            case STATE_ATTACKING:
                if(enemyToAttack != null){
                    attackEnemy();
                } else{
                    player.setCurrentEntityState(GameEntity.EntityState.STATE_READY);
                }
                break;
            case STATE_DYING:
                break;
            case STATE_MOVING:
                break;
            case STATE_FLYING:
                break;
            case STATE_READY:
                if(leftIsPressed){
                    enemyToAttack = findClosestEnemy(true);
                } else if(rightIsPressed){
                    enemyToAttack = findClosestEnemy(false);
                }
                if(enemyToAttack != null){
                    player.setCurrentEntityState(GameEntity.EntityState.STATE_ATTACKING);
                }
                break;
            default:
                break;
        }

    }

    private static void attackEnemy(){
        float xSpeed = player.getXSpeed();
        float ySpeed = 0;

        boolean isToLeft = true;

        if (isToTheRight(player, enemyToAttack)) {
            xSpeed = -xSpeed;
            isToLeft = false;
        }
        Rectangle enemyRect = new Rectangle(enemyToAttack.getX() + xSpeed,
                enemyToAttack.getY() + ySpeed,
                GameEntity.getWidth(),
                GameEntity.getHeight());

        if(isColliding(enemyRect, player.getPlayerRect())){
            enemyToAttack.takeDamage();
            if(enemyToAttack.getHealth() <= 0){
                killAndThrowEnemy(enemyToAttack);
            }
            enemyToAttack = null;
        } else{
            movePlayer(player.getXSpeed(), 0);
        }
    }

    private static void killAndThrowEnemy(Enemy enemyToAttack) {
    }

    private static void movePlayer(float xSpeed, float ySpeed){
        for (Enemy enemy : enemies) {
            enemy.setX((int)(enemy.getX() + xSpeed));
        }
        level.move(xSpeed, ySpeed);
    }

    private static boolean isColliding(Rectangle rect1, Rectangle rect2){

        return rect1.overlaps(rect2);
    }

    public static boolean isToTheLeft(Player player, Enemy enemy){
        return enemy.getX() < player.getX();
    }
    public static boolean isToTheRight(Player player, Enemy enemy){
        return enemy.getX() > player.getX() + player.getWidth();
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
        return enemies.stream().
                filter(entity -> enemyIsInRange(playerX, range, entity.getX())).
                sorted((enemy1, enemy2) -> toLeft ? (enemy1.getX() - enemy2.getX()) : (enemy2.getX() - enemy1.getX())).
                findFirst().
                orElse(null);
    }

    /**
     * Checks weather an enemy is in the range given by a startpos a width and a position of the
     * entitiy to check if it is in range.
     * @param startPos the start position of the range check
     * @param width the range
     * @param x the position of the entitiy that might be in range.
     * @return if the x position is inside the range.
     */
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

    public static void doPhysicsStep(float deltaTime){
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            physicsWorld.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= TIME_STEP;
        }
    }

    private static void renderPlayer(SpriteBatch batch) {
        flyweightMap.get(player.getClass()).setPosition(player.getX(), player.getY());
        flyweightMap.get(player.getClass()).draw(batch);

    }

    private static void renderEnemies(SpriteBatch batch) {
        for (GameEntity enemy : enemies) {
            flyweightMap.get(enemy.getClass()).setPosition(enemy.getX(), enemy.getY());
            flyweightMap.get(enemy.getClass()).draw(batch);
        }
    }

    private static void renderBackground(SpriteBatch batch) {
        level.getBackground().draw(batch);
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
            flyweightMap.get(textureClasses).getTexture().dispose();
        }
        flyweightMap.get(player.getClass()).getTexture().dispose();
        shapeRenderer.dispose();
    }
}

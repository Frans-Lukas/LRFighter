package com.mygdx.lrgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.lrgame.GameLoop;
import com.mygdx.lrgame.drawables.entities.GameEntity;
import com.mygdx.lrgame.drawables.entities.enemy.Enemy;
import com.mygdx.lrgame.drawables.entities.enemy.EnemyRagdoll;
import com.mygdx.lrgame.drawables.entities.player.Player;
import com.mygdx.lrgame.drawables.levels.Level;

import java.util.ArrayList;
import java.util.Random;

import static com.mygdx.lrgame.GameLoop.GAME_HEIGHT;
import static com.mygdx.lrgame.GameLoop.GAME_WIDTH;

public class Model {


    private static final int PLAYER_HEALTH = 3;

    private static final int BASIC_ENEMY_HEALTH = 1;
    private static final int ENEMY_START_POS_Y = GAME_HEIGHT / 2;
    public static final int X_FORCE_VARIANCE = 400;
    public static final int Y_FORCE_VARIANCE = 100;
    private static final float PLAYER_X_FORCE = 650f;
    private static final float PLAYER_Y_FORCE = 200f;
    private static final float SPAWN_TIMER = 1;
    private static final float PHYSICS_TIME_STEP = 1/300f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    private static float actualSpawnTimer = 1;
    private static float currentTime = 0;
    private static boolean spawnAtLeftSide;

    private static Enemy enemyToAttack;
    private Player player;
    private ArrayList<Enemy> enemies;
    private static ArrayList<EnemyRagdoll> ragdolls;
    private static Level level;

    private static World physicsWorld;
    private static Box2DDebugRenderer debugRenderer;
    private static float accumulator;

    private static Random randGenerator;

    private static boolean leftIsPressed;
    private static boolean rightIsPressed;
    private static boolean resetLevel;


    public Model() {
        enemies = new ArrayList<>();
        ragdolls = new ArrayList<>();
        randGenerator = new Random();

        player = new Player(PLAYER_HEALTH, GAME_WIDTH / 2, GAME_HEIGHT / 2);
        enemyToAttack = null;
        spawnAtLeftSide = true;

        physicsWorld = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        accumulator = 0;

        level = new Level();

        Enemy enemyLeft = new Enemy(BASIC_ENEMY_HEALTH, 0, ENEMY_START_POS_Y);
        Enemy enemyRight = new Enemy(BASIC_ENEMY_HEALTH, GAME_WIDTH, ENEMY_START_POS_Y);
        enemyLeft.setCurrentEntityState(GameEntity.EntityState.STATE_ATTACKING);
        enemyRight.setCurrentEntityState(GameEntity.EntityState.STATE_ATTACKING);

        enemies.add(enemyLeft);
        enemies.add(enemyRight);
    }

    public void update() {
        /**
         * Create the player and a pool of entities.
         * Create a flyweight hashmap that the pool of entities can
         * get their textures from.
         */

        level.update();
        updatePlayer();
        updateEnemies();
        if(resetLevel){
            GameLoop.setUp();
        }

        updateRagdolls();

        updateLevel();

        input();

    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.update(player);
        }
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
     * Update all ragdolls according to their state.
     */
    public void updateRagdolls() {
        for (int i = 0; i < ragdolls.size(); i++) {
            EnemyRagdoll ragdoll = ragdolls.get(i);
            ragdoll.update();
            GameEntity.EntityState ragdollState = ragdoll.getFromEnemy().getCurrentEntityState();
            float x = ragdoll.getRagdollBody().getPosition().x;
            float y = ragdoll.getRagdollBody().getPosition().y;

            switch (ragdollState){

                //Remove enemy object when out of screen.
                case STATE_DYING:
                    if (x > GAME_WIDTH + GameEntity.getWidth() || x < -GameEntity.getWidth()) {
                        ragdolls.remove(ragdoll);
                        i--;
                    }
                    break;
                case STATE_FLYING:
                    //Recover enemy when ragdoll hits ground.
                    if(y >= ENEMY_START_POS_Y){
                        ragdolls.remove(ragdoll);
                        Enemy recoveredEnemy = ragdoll.getFromEnemy();
                        recoveredEnemy.setY(ENEMY_START_POS_Y);
                        enemies.add(recoveredEnemy);
                    }
                    break;
            }
        }
    }

    public static void input(){
        leftIsPressed = Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isKeyPressed(Input.Keys.LEFT);
        rightIsPressed = Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        resetLevel = Gdx.input.isKeyPressed(Input.Keys.R);

    }

    /**
     * Update player according to state
     */
    public void updatePlayer() {

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

    public void attackEnemy(){
        float xSpeed = player.getXSpeed();
        float ySpeed = 0;
        boolean isToTheLeft = true;
        if (isToTheRight(player, enemyToAttack)) {
            xSpeed = -xSpeed;
            isToTheLeft = false;
        }
        Rectangle enemyRect = new Rectangle(enemyToAttack.getX() + xSpeed,
                enemyToAttack.getY() + ySpeed,
                GameEntity.getWidth(),
                GameEntity.getHeight());
        Rectangle playerRect = player.getPlayerRect();

        if(isColliding(enemyRect, playerRect)){
            enemyToAttack.takeDamage();
            if(enemyToAttack.getHealth() <= 0){
                killAndThrowEnemy(enemyToAttack, isToTheLeft);
            }
            enemyToAttack = null;
        } else{
            movePlayer(xSpeed, 0);
        }
    }

    private void killAndThrowEnemy(Enemy enemyToAttack, boolean enemyIsToLeft) {
        enemies.remove(enemyToAttack);
        enemyToAttack.setCurrentEntityState(GameEntity.EntityState.STATE_DYING);

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(enemyToAttack.getX() + GameEntity.getWidth() / 2, enemyToAttack.getY() + GameEntity.getHeight() / 2);
        Body body = physicsWorld.createBody(bDef);

        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(GameEntity.getWidth() - 1.75f, GameEntity.getWidth() - 1.75f);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = rectangle;
        fixDef.density = 0.5f;
        fixDef.friction = 0.0f;

        //this is needed for some reason?
        Fixture fixture = body.createFixture(fixDef);

        float appliedXForce = (enemyIsToLeft ? -PLAYER_X_FORCE : PLAYER_X_FORCE) + randGenerator.nextInt(X_FORCE_VARIANCE) - X_FORCE_VARIANCE;
        float appliedYForce = PLAYER_Y_FORCE + randGenerator.nextInt(Y_FORCE_VARIANCE) - Y_FORCE_VARIANCE;
        body.applyLinearImpulse(appliedXForce, appliedYForce, body.getPosition().x, body.getPosition().y, true);

        ragdolls.add(new EnemyRagdoll(body, enemyToAttack));

        rectangle.dispose();
    }

    private void movePlayer(float xSpeed, float ySpeed){
        for (Enemy enemy : enemies) {
            enemy.setX((int)(enemy.getX() + xSpeed));
        }
        level.move(xSpeed, ySpeed);
    }

    private boolean isColliding(Rectangle rect1, Rectangle rect2){
        return rect1.overlaps(rect2);
    }

    public boolean isToTheLeft(Player player, Enemy enemy){
        return enemy.getX() < player.getX();
    }
    public boolean isToTheRight(Player player, Enemy enemy){
        return enemy.getX() > player.getX() + player.getWidth();
    }

    /**
     * Find the closeset enemy either to the right or to the left of the player according
     * to the player range
     * @param toLeft if method should look to the left or right.
     * @return the enemy that is close, or null if no enemy is within range.
     */
    private Enemy findClosestEnemy(final boolean toLeft) {
        //if toLeft get left half of screen else get right half of screen.
        final float range = toLeft ? -player.getRange() : player.getRange();

        //if toLeft get x pos of left side of player else get right x pos of player
        final float playerX = toLeft ? player.getX() : player.getX() + player.getWidth();

        //return closest enemy from the list.
        return enemies.stream().
                filter(entity -> enemyIsInRange(playerX, range, entity.getX())).
                sorted((enemy1, enemy2) -> (int) (toLeft ? (enemy1.getX() - enemy2.getX()) : (enemy2.getX() - enemy1.getX()))).
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
    private boolean enemyIsInRange(float startPos, float width, float x) {
        float leftPoint = Math.min(startPos, startPos + width);
        float rightPoint = Math.max(startPos, startPos + width);
        return x >= leftPoint && x <= rightPoint;
    }

    public void doPhysicsStep(float deltaTime, OrthographicCamera camera){

        //debugRenderer.render(physicsWorld, camera.combined);
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= PHYSICS_TIME_STEP) {
            physicsWorld.step(PHYSICS_TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            accumulator -= PHYSICS_TIME_STEP;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<EnemyRagdoll> getRagdolls() {
        return ragdolls;
    }

    public Level getLevel() {
        return level;
    }

}

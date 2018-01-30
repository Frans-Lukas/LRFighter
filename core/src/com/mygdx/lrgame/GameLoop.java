package com.mygdx.lrgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.lrgame.drawables.entities.enemy.Enemy;
import com.mygdx.lrgame.drawables.entities.GameEntity;
import com.mygdx.lrgame.drawables.entities.enemy.EnemyModifier;
import com.mygdx.lrgame.drawables.entities.enemy.EnemyRagdoll;
import com.mygdx.lrgame.drawables.levels.Level;
import com.mygdx.lrgame.drawables.entities.player.Player;
import com.mygdx.lrgame.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameLoop {

    public static final int GAME_HEIGHT = 100;
    public static final int GAME_WIDTH = 100;

    private static HashMap<Class, Sprite> flyweightMap;

    private static Model model;

    private static ShapeRenderer shapeRenderer = new ShapeRenderer();
    private static Color rangeIndicatorLeftColor = Color.RED;
    private static Color rangeIndicatorRightColor = Color.BLUE;

    public static void setUp(){

        model = new Model();
        flyweightMap = new HashMap<>();

        Sprite playerSprite = new Sprite(new Texture("Player.png"));
        playerSprite.setSize(GameEntity.getWidth(), GameEntity.getHeight());

        Sprite enemySprite = new Sprite(new Texture("Enemy.png"));
        enemySprite.setSize(GameEntity.getWidth(), GameEntity.getHeight());

        flyweightMap.put(Player.class, playerSprite);
        flyweightMap.put(Enemy.class, enemySprite);

    }

    public static void Update(){
        model.update();

    }

    public static void Render(SpriteBatch batch, OrthographicCamera cam){
        //Always draw level background first.
        renderBackground(batch);
        //Then range indicators
        renderRange(batch, cam);
        //Then Enemies.
        renderEnemies(batch);
        //Lastly player
        renderPlayer(batch);
    }



    private static void renderPlayer(SpriteBatch batch) {
        Player player = model.getPlayer();
        flyweightMap.get(player.getClass()).setPosition(player.getX(), player.getY());
        flyweightMap.get(player.getClass()).draw(batch);

    }

    private static void renderEnemies(SpriteBatch batch) {
        for (GameEntity enemy : model.getEnemies()) {
            flyweightMap.get(enemy.getClass()).setPosition(enemy.getX(), enemy.getY());
            flyweightMap.get(enemy.getClass()).draw(batch);
        }

        for (EnemyRagdoll ragdoll : model.getRagdolls()) {
            flyweightMap.get(Enemy.class).setPosition(ragdoll.getRagdollBody().getPosition().x - GameEntity.getWidth() / 2,
                                                    ragdoll.getRagdollBody().getPosition().y - GameEntity.getWidth() / 2);
            flyweightMap.get(Enemy.class).draw(batch);
        }
    }

    private static void renderBackground(SpriteBatch batch) {
        model.getLevel().render(batch);
    }

    /**
     * Render the range indicators for where the player can reach.
     * @param batch
     */
    private static void renderRange(SpriteBatch batch, OrthographicCamera cam){
        batch.end();

        Player player = model.getPlayer();

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(rangeIndicatorRightColor);
        shapeRenderer.line(player.getX() + GameEntity.getWidth() + player.getRange(), GAME_HEIGHT / 2,
                player.getX() + GameEntity.getWidth() + player.getRange(), -GAME_HEIGHT);


        shapeRenderer.setColor(rangeIndicatorLeftColor);
        shapeRenderer.line(player.getX() - player.getRange(), GAME_HEIGHT / 2,
                player.getX() - player.getRange(), -GAME_HEIGHT);

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
        flyweightMap.get(Player.class).getTexture().dispose();
        shapeRenderer.dispose();
    }

    public static void doPhysicsStep(float deltaTime, OrthographicCamera cam) {
        model.doPhysicsStep(deltaTime, cam);
    }
}

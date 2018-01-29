package com.mygdx.lrgame.drawables.entities.enemy;

import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.lrgame.drawables.entities.GameEntity;

public class EnemyRagdoll {
    private Body ragdollBody;
    private Enemy fromEnemy;

    public EnemyRagdoll(Body ragdollBody, Enemy fromEnemy) {
        this.ragdollBody = ragdollBody;
        this.fromEnemy = fromEnemy;
    }

    public Body getRagdollBody() {
        return ragdollBody;
    }

    public void update(){
        if(fromEnemy.getCurrentEntityState() == GameEntity.EntityState.STATE_DYING){

        }
    }

    public Enemy getFromEnemy() {
        return fromEnemy;
    }
}

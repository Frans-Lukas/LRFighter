package com.mygdx.lrgame.drawables.entities.enemy;

import com.badlogic.gdx.physics.box2d.Body;

public class EnemyRagdoll {
    private Body ragdollBody;

    public EnemyRagdoll(Body ragdollBody) {
        this.ragdollBody = ragdollBody;
    }

    public Body getRagdollBody() {
        return ragdollBody;
    }

    public void update(){

    }
}

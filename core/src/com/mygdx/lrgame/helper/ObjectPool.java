package com.mygdx.lrgame.helper;

import com.mygdx.lrgame.drawables.entities.enemy.Enemy;
import com.mygdx.lrgame.drawables.entities.GameEntity;

import java.util.ArrayList;
import java.util.List;

public class ObjectPool {
    private static List<Enemy> enemyPool = new ArrayList();

    public static GameEntity acquireEntity(){
        if(enemyPool.isEmpty()){
            return new Enemy(1, 0,0);
        }
        Enemy e = enemyPool.get(0);
        enemyPool.remove(0);
        return e;
    }

}

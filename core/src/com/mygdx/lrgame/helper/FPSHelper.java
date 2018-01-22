package com.mygdx.lrgame.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

import java.util.ArrayList;

public class FPSHelper{
    private static float averageDeltaTime = 0;
    private final static int numAveragesToTake = 10;
    private static int counter = 0;
    private final static int upperBound = 3000;
    private static ArrayList<Float> deltas = new ArrayList<Float>();

    /**
     * Method that always returns the average of the latest 10 deltaTimes
     * @return the latest 10 delta times.
     */
    private static float getDeltaTime(){
        if(counter >= numAveragesToTake){
            averageDeltaTime -= deltas.get(0);
            counter = numAveragesToTake;
        }
        deltas.add(Gdx.graphics.getDeltaTime());
        averageDeltaTime = (averageDeltaTime + deltas.get(counter)) / (counter + 1);
        counter++;
        return Gdx.graphics.getDeltaTime();
    }
}

package com.example.thomas.voyage;

import android.content.Context;
import android.util.Log;

/**
 * Created by Don Maus on 22-Jul-15.
 */
public class Hero {

    private String heroName;
    private int hitPoints;
    private String classPrimary;
    private String classSecondary;
    private int costs;

    //Konstruktor, Initialize seperat für spätere Zwecke


    public void Initialize() {

        HeroPool heropool = new HeroPool();

        heroName = HeroPool.setName();
        classPrimary = HeroPool.setClassPrimary();
        hitPoints = HeroPool.setHitPoints();
        classSecondary = HeroPool.setClassSecondary();
        costs = heropool.getCosts();
    }

    public String getHeroData() {


        String string = heroName + " " + classPrimary + " " + hitPoints + " " + classSecondary + " " + costs;
        Log.i("get", "getHeroData:_heroname " + string);
        return string;
    }

}

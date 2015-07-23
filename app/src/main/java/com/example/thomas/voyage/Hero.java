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

    //Konstruktor, Initialize seperat für spätere Zwecke

    //für debug noch immer auf "null"

    public void Initialize(String biome) {        //Platzhalter: in Klammer, welches Biome (in Blöcken)

        HeroPool heropool = new HeroPool();

        heroName = HeroPool.setName();
        /*
        classPrimary=heropool.setClassPrimary(biome);
        hitPoints = heropool.setHitPoints();
        classSecondary=heropool.setClassSecondary();
        */

    }

    public String getHeroData() {


        String string = heroName + " " + classPrimary + " " + hitPoints + " " + classSecondary;
        Log.i("get", "getHeroData:_heroname " + string);
        return string;
    }

}

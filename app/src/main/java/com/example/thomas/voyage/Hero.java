package com.example.thomas.voyage;

import android.content.Context;
import android.util.Log;


public class Hero {

    Context context;

    private String heroName;
    private int hitPoints;
    private String classPrimary;
    private String classSecondary;
    private int costs;
    private String imageResource;

    //Konstruktor, Initialize seperat für spätere Zwecke

    public Hero(Context con){
        context = con;
    }


    public void Initialize(String merchantBiome) {

        HeroPool heropool = new HeroPool(context);

        heroName = HeroPool.setName();
        classPrimary = HeroPool.setClassPrimary(merchantBiome);
        hitPoints = HeroPool.setHitPoints();
        classSecondary = HeroPool.setClassSecondary();
        costs = heropool.getCosts();
        imageResource = heropool.getImageResource();
    }

    public String getStrings(String identifier) {

        String string;

        switch (identifier){
            case "heroName":
                string = heroName; break;
            case "classPrimary":
                string = classPrimary; break;
            case "classSecondary":
                string = classSecondary; break;
            case "imageResource":
                string = imageResource; break;
            default:
                string = "EVIL"; break;
        }

        return string;
    }

    public int getInts(String identifier){

        int returnValue;

        switch(identifier){
            case "hitpoints":
                returnValue = hitPoints; break;
            case "costs":
                returnValue = costs; break;
            default:
                returnValue = 666; break;
        }

        return returnValue;
    }

}

package com.example.thomas.voyage;

import android.content.Context;
import android.util.Log;


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

    public String getStrings(String identifier) {

        String string;

        switch (identifier){
            case "heroName":
                string = heroName; break;
            case "classPrimary":
                string = classPrimary; break;
            case "classSecondary":
                string = classSecondary; break;
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

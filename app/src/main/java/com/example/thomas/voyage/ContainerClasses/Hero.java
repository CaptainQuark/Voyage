package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;

import com.example.thomas.voyage.ResClasses.ConstRes;


public class Hero {

    Context context;

    ConstRes co;

    private String heroName, classSecondary, classPrimary, imageResource;
    private int hp, costs, hpConst;

    //Konstruktor, Initialize seperat für spätere Zwecke
    public Hero(Context con){
        context = con;
        co = new ConstRes();
    }


    // 2. Konstruktor, welcher gewählt werden kann (jedoch immer nur einer)
    public Hero(String name, String prime, String sec, String imgRes, int hp, int costs){
        co = new ConstRes();
        heroName = name;
        classPrimary = prime;
        classSecondary = sec;
        imageResource = imgRes;
        this.hp = hp;
        this.costs = costs;

        hpConst = hp;
    }

    public void Initialize(String merchantBiome) {

        HeroPool heropool = new HeroPool(context);

        heroName = HeroPool.setName();
        classPrimary = HeroPool.setClassPrimary(merchantBiome);
        hp = HeroPool.setHitPoints();
        classSecondary = HeroPool.setClassSecondary();
        costs = heropool.getCosts();
        imageResource = heropool.getImageResource();

        hpConst = hp;

        //heroName = "JA"; classPrimary = ""; classSecondary = ""; hp = -100; costs = 500; imageResource = "hero_dummy_0";
    }

    public String getHeroName(){
        return heroName;
    }

    public String getClassPrimary(){
        return classPrimary;
    }

    public String getClassSecondary(){
        return classSecondary;
    }

    public String getImageResource(){
        return imageResource;
    }

    public int getHp(){
        return hp;
    }

    public int getHpConst(){
        return hpConst;
    }

    public int getCosts(){
        return costs;
    }

    public String getStrings(String identifier) {

        String val;

        switch (identifier){
            case "heroName":
                val = heroName; break;
            case "classPrimary":
                val = classPrimary; break;
            case "classSecondary":
                val = classSecondary; break;
            case "imageResource":
                val = imageResource; break;
            default:
                val = "EVIL"; break;
        }

        return val;
    }

    public int getInts(String identifier){

        int val;

        switch(identifier){
            case "hp":
                val = hp; break;
            case "hpConst":
                val = hpConst; break;
            case "costs":
                val = costs; break;
            default:
                val = 666; break;
        }

        return val;
    }

    public void setInt(String id, int val){

        switch (id){
            case "hp":
                hp = val; break;
            case "costs":
                costs = val; break;
        }
    }

}

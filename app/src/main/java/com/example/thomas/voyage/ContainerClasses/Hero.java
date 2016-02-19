package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;

import com.example.thomas.voyage.ResClasses.ConstRes;


public class Hero {

    Context context;

    ConstRes co;

    private String heroName, classSecondary, classPrimary, imageResource;
    private int hp, costs, hpTotal, evasion, bonusNumber;

    //Konstruktor, Initialize seperat für spätere Zwecke
    public Hero(Context con) {
        context = con;
        co = new ConstRes();
    }


    // 2. Konstruktor, welcher gewählt werden kann (jedoch immer nur einer)
    public Hero(String name, String prime, String sec, String imgRes, int hp, int hpTotal, int costs, int evasion, int bonusNumber) {
        co = new ConstRes();
        heroName = name;
        classPrimary = prime;
        classSecondary = sec;
        imageResource = imgRes;
        this.hp = hp;
        this.hpTotal = hpTotal;
        this.costs = costs;
        this.evasion = evasion;
        this.bonusNumber = bonusNumber;
    }

    public void Initialize(String merchantBiome) {

        HeroPool heropool = new HeroPool(context);

        //Ob-8: Reihenfolge sehr wichtig!
        heroName = heropool.setName();
        classSecondary = heropool.setClass(merchantBiome, "Secondary", context);
        classPrimary = heropool.setClass(merchantBiome, "Primary", context);
        hpTotal = heropool.setHitPoints();
        costs = heropool.setCosts();
        evasion = heropool.setEvasion();
        imageResource = heropool.getImageResource();
        bonusNumber = heropool.setBonusNumber();
        hp = hpTotal;

        //heroName = "JA"; classPrimary = ""; classSecondary = ""; hp = -100; costs = 500; imageResource = "hero_dummy_0";
    }

    public String getHeroName() {return heroName;}

    public String getClassPrimary() {
        return classPrimary;
    }

    public String getClassSecondary() {
        return classSecondary;
    }

    public String getImageResource() {
        return imageResource;
    }

    public int getHp() {
        return hp;
    }

    public int getHpTotal() {
        return hpTotal;
    }

    public int getCosts() { return costs; }

    public int getBonusNumber() { return bonusNumber; }

    public int getEvasion() { return evasion; }

    public void setHp(int hp){ this.hp = hp; }
}
package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;

import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.Databases.DBmerchantHeroesAdapter;
import com.example.thomas.voyage.ResClasses.ConstRes;


public class Hero {

    Context con;
    ConstRes c;

    private String heroName, classSecondary, classPrimary, imageResource;
    private int hp, costs, hpTotal, evasion, bonusNumber;

    //Konstruktor, Initialize seperat für spätere Zwecke
    public Hero(Context con) {
        this.con = con;
        c = new ConstRes();
    }


    // 2. Konstruktor, welcher gewählt werden kann (jedoch immer nur einer)
    public Hero(String name, String prime, String sec, String imgRes, int hp, int hpTotal, int costs, int evasion, int bonusNumber) {
        c = new ConstRes();
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

        HeroPool heropool = new HeroPool(con);

        //Ob-8: Reihenfolge sehr wichtig!
        heroName = heropool.setName();
        classSecondary = heropool.setClass(merchantBiome, "Secondary", con);
        classPrimary = heropool.setClass(merchantBiome, "Primary", con);
        hpTotal = heropool.setHitPoints();
        costs = heropool.setCosts();
        evasion = heropool.setEvasion();
        imageResource = heropool.getImageResource();
        bonusNumber = heropool.setBonusNumber();
        hp = hpTotal;

        //heroName = "JA"; classPrimary = ""; classSecondary = ""; hp = -100; costs = 500; imageResource = "hero_dummy_0";
    }

    public void copyHeroDataFromPlayerDatabase(int i){
        DBheroesAdapter h = new DBheroesAdapter(con);

        heroName = h.getHeroName(i);
        classPrimary = h.getHeroPrimaryClass(i);
        classSecondary = h.getHeroSecondaryClass(i);
        imageResource = h.getHeroImgRes(i);
        hp = h.getHeroHitpoints(i);
        hpTotal = h.getHeroHitpointsTotal(i);
        costs = h.getHeroCosts(i);
        evasion = h.getHeroEvasion(i);
        bonusNumber = h.getHeroBonusNumber(i);
    }

    public void copyHeroDataFromMerchantDatabase(int i){
        DBmerchantHeroesAdapter m = new DBmerchantHeroesAdapter(con);

        heroName = m.getHeroName(i);
        classPrimary = m.getHeroPrimaryClass(i);
        classSecondary = m.getHeroSecondaryClass(i);
        imageResource = m.getHeroImgRes(i);
        hp = m.getHeroHitpoints(i);
        hpTotal = m.getHeroHitpointsTotal(i);
        costs = m.getHeroCosts(i);
        evasion = m.getHeroEvasion(i);
        bonusNumber = m.getHeroBonusNumber(i);
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
package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;

public class Monster {

    public String name = "", checkout = "", imgRes = "", monsterDifficulty = "";
    public double  resistance = -1, critMultiplier = -1;
    public int hp, hpTotal, dmgMin = -1, dmgMax = -1, armor = 0, evasion = -1, critChance = -1, bounty = -1, accuracy = 0;

    public Monster(String currentBiome, String difficulty, Context c){
        MonsterPool monsterPool = new MonsterPool(currentBiome, difficulty, c);

        name = monsterPool.getName();
        checkout = monsterPool.getCheckout();
        evasion = monsterPool.getEvasion();
        accuracy = monsterPool.getAccuracy();
        critChance = monsterPool.getCritChance();
        critMultiplier = monsterPool.getCritMultiplier();
        hp = monsterPool.getHp();
        hpTotal = hp;
        dmgMin = monsterPool.getDmgMin();
        dmgMax = monsterPool.getDmgMax();
        imgRes = monsterPool.getImgRes();
        resistance = monsterPool.getResistance();
        armor = monsterPool.getArmor();
        bounty = monsterPool.getBounty();
        monsterDifficulty = monsterPool.getDifficulty();
    }

    public Monster(String tName, String check, String imgRes, int eva, int acc, int crit, int tHp, int tHpTotal, int tDmgMin, int tDmgMax, int armor, double resistance, double critMultiplier, String diff, int bounty){
        name = tName;
        checkout = check;
        this.imgRes = imgRes;
        evasion = eva;
        accuracy = acc;
        critChance = crit;
        hp = tHp;
        hpTotal = tHpTotal;
        dmgMin = tDmgMin;
        dmgMax = tDmgMax;
        this.armor = armor;
        this.resistance = resistance;
        this.critMultiplier = critMultiplier;
        monsterDifficulty = diff;
        this.bounty = bounty;
    }
}

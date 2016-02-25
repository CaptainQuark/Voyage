package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.util.Log;

public class Monster {

    public String name = "", checkout = "", imgRes = "", monsterDifficulty = "";
    public double  resistance = -1, critMultiplier = -1, accuracy = 0;
    public int hp, hpTotal, dmgMin = -1, dmgMax = -1, block = 0, evasion = -1, critChance = -1, bounty = -1;

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
        block = monsterPool.getBlock();
        bounty = monsterPool.getBounty();
        monsterDifficulty = monsterPool.getDifficulty();
    }

    public Monster(String tName, String check, String imgRes, int eva, double acc, int crit, int tHp, int tHpTotal, int tDmgMin, int tDmgMax, int block, double resistance, double critMultiplier, String diff, int bounty){
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
        this.block = block;
        this.resistance = resistance;
        this.critMultiplier = critMultiplier;
        monsterDifficulty = diff;
        this.bounty = bounty;
    }
}

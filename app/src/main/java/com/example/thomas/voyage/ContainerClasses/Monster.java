package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.util.Log;

public class Monster {

    public String name = "", checkout = "", imgRes = "";
    public double  resistance = -1, critMultiplier = -1;
    public int hp, hpTotal, dmgMin = -1, dmgMax = -1, block = 0, evasion = -1, accuracy = -1, critChance = -1;

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
    }

    public Monster(String tName, String check, int eva, int acc, int crit, int tHp, int tHpTotal, int tDmgMin, int tDmgMax, int block, double resistance, double critMultiplier){
        name = tName;
        checkout = check;
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
    }
}

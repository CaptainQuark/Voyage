package com.example.thomas.voyage.ContainerClasses;

import android.util.Log;

public class Monster {

    private String name = "", checkout = "", imgRes = "";
    private int evasion = -1, accuracy = -1, critChance = -1, hp = 500, dmgMin = -1, dmgMax = -1;

    public Monster(){
        MonsterPool monsterPool = new MonsterPool();

        name = monsterPool.getName();
        checkout = monsterPool.getCheckout();
        evasion = monsterPool.getEvasion();
        accuracy = monsterPool.getAccuracy();
        critChance = monsterPool.getCritChance();
        hp = monsterPool.getHp();
        dmgMin = monsterPool.getDmgMin();
        dmgMax = monsterPool.getDmgMax();
        imgRes = monsterPool.getImgRes();
    }

    public Monster(String tName, String check, int eva, int acc, int crit, int tHp, int tDmgMin, int tDmgMax){
        name = tName;
        checkout = check;
        evasion = eva;
        accuracy = acc;
        critChance = crit;
        hp = tHp;
        dmgMin = tDmgMin;
        dmgMax = tDmgMax;
    }

    public String getString(String id){

        String val = id;

        switch (id){

            case "name":
                val = name;
                break;
            case "checkout":
                val = checkout;
            default:
                Log.e("MONSTER_GET_STRING", "Error@ getString, default called");
                break;
        }

        return val;
    }

    public String getImgRes(){
        return imgRes;
    }

    public int getInt(String id){

        int val = -1;

        switch (id){

            case "evasion":
                val = evasion; break;
            case "accuracy":
                val = accuracy; break;
            case "critChance":
                val = critChance; break;
            case "hp":
                val = hp; break;
            case "dmgMin":
                val = dmgMin; break;
            case "dmgMax":
                val = dmgMax; break;
            default:
                Log.e("MONSTER_GET_INT", "Error@ getInt, default called");
        }

        return val;
    }

    public void setInt(String id, int val){

        switch (id){

            case "evasion":
                evasion = val; break;
            case "accuracy":
                accuracy = val; break;
            case "critChance":
                critChance = val; break;
            case "hp":
                hp = val; break;
            case "dmgMin":
                dmgMin = val; break;
            case "dmgMax":
                dmgMax = val; break;
            default:
                Log.e("MONSTER_GET_INT", "Error@ getInt, default called");
        }
    }
}

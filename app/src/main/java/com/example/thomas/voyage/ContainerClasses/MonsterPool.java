package com.example.thomas.voyage.ContainerClasses;

public class MonsterPool {

    public String getName(){
        String[] val = {"Magmamemnnon, Knöllchen, Steinchen"};
        int size = val.length, rarity = 0;

        for(boolean check = true; check; ){
            rarity = (int) (Math.random() * 100);
            if(rarity <= size){check = false;}
        }

        return val[rarity];
    }

    public String getCheckout(){
        String val = "setCheckout";

        return val;
    }

    public int getEvasion(){
        int val = -1;

        return val;
    }

    public int getAccuracy(){
        int val = -1;

        return val;
    }

    public int getCritChance(){
        int val = -1;

        return val;
    }

    public int getHp(){
        int val = 500;

        return val;
    }

    public int getDmgMin(){
        int val = -1;

        return val;
    }

    public int getDmgMax(){
        int val = -1;

        return val;
    }
}

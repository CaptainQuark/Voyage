package com.example.thomas.voyage.ContainerClasses;
import java.util.Random;

public class MonsterPool {

    public String getName(){
        Random rand = new Random();
        String[] names = {"Magmamemnnon", "Knöllchen", "Steinchen"};
        int size = names.length, index = 0;

        for(boolean check = true; check; ){

            // erzeuge Zufallswert von 0 - 2
            // wenn zwischen 5 - 10, dann: int randomValue = ran.nextInt(6) + 5;
            //  -> Randomwert zw. 0 und 5 wird mit 5 addiert
            index = rand.nextInt(size);
            check = false;
        }

        return names[index];
    }

    public String getCheckout(){
        String val = "double";

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

package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;

import java.util.Random;

public class ItemPool {

    private Context c;

    public ItemPool(Context context){
        c = context;
    }

    public int getSkillId(){

        return -1;
    }

    public int getBuyCosts(){
        Random rand = new Random();
        return rand.nextInt(200)+50;
    }

    public int getSpellCosts(){

        return -1;
    }

    public String getName(){
        String name = "NOT_USED";

        String[] names = {"Itemtrank", "Glump!", "Goga-Goga", "Pupsi", "Brise Birse", "Johnny-Holzfäller-Bottle", "Soda 'Zisch'", "Flying Jürgen", "Mineralsteinwasser",
                "Ein Produkt der Marke 'Schlürf'", "Romaneé-Conti Domaine de la Romaneé-Conti"};

        for(boolean run = true; run;){
            int val = (int) (Math.random() * 100);

            if(val < names.length && val >= 0){
                name = names[val];
                run = false;
            }
        }

        return name;
    }

    public String getDesMain(){

        return "Description main";
    }

    public String getDesAdd(){

        return "Description additional";
    }

    public String getRarity(){

        return "Rarity";
    }
}

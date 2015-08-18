package com.example.thomas.voyage;

import android.content.Context;

import java.util.Random;

public class ItemPool {

    Context c;

    public ItemPool(Context context){
        c = context;
    }

    public int getSkillId(){

        return -1;
    }

    public int getBuyCosts(){

        return -1;
    }

    public int getSpellCosts(){

        return -1;
    }

    public String getName(){
        String name = "NOT_USED";

        String[] names = {"Itemtrank", "Glump!", "Goga-Goga", "Pupsi", "Brise Birse", "Johnny-Holzfäller-Bottle", "Soda 'Zisch'"};

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

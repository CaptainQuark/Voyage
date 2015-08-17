package com.example.thomas.voyage;

import android.content.Context;

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

        return "Item";
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

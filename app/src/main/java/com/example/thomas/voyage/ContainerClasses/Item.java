package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;


public class Item {

    private static Context c;
    private ItemPool ip = new ItemPool(c);
    private String name, desMain, desAdd, rarity;
    private int skillId, buyCosts, spellCosts;

    public Item(Context context){
        c = context;
        //this.initializeItemByPool();
    }

    public Item(String name, String desMain, String desAdd, String rarity, int skillId, int buyCosts, int spellCosts){
        this.name = name;
        this.desMain = desMain;
        this.desAdd = desAdd;
        this.rarity = rarity;
        this.skillId = skillId;
        this.buyCosts = buyCosts;
        this.spellCosts = spellCosts;
    }

    private void initializeItemByPool(){
        // vielleicht für später, Nutzen noch nicht nicht wirklich ersichtlich...
        // Beachte: 'private' = von außen nicht zugreifbar
    }

    public String getStrings(String identifier) {

        String val;

        switch (identifier){
            case "ITEM_NAME":
                val = ip.getName(); break;
            case "DES_MAIN":
                val = ip.getDesMain(); break;
            case "DES_ADD":
                val = ip.getDesAdd(); break;
            case "RARITY":
                val = ip.getRarity(); break;
            default:
                val = "EVIL"; break;
        }

        return val;
    }

    public int getInts(String identifier){

        int val;

        switch(identifier){
            case "SKILL_ID":
                val = ip.getSkillId(); break;
            case "BUY_COSTS":
                val = ip.getBuyCosts(); break;
            case "SPELL_COSTS":
                val = ip.getSpellCosts(); break;
            default:
                val = 666; break;
        }

        return val;
    }


}

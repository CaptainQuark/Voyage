package com.example.thomas.voyage.ContainerClasses;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Random;

public class MonsterPool {

    private int rarity;
    private int hp;
    private int dmgMin;
    private int dmgMax;
    private int armor;
    private int evasion;
    private int critChance;
    private int bounty;
    private int accuracy;

    private double resistance;
    private double critMultiplier;

    private String checkout;
    private String name;
    private String monsterdifficulty;

    private boolean run = true;

    public MonsterPool(String currentBiome, String difficulty, Context context ){

        rarity = (int) (Math.random() * 100);           //Wählt eine der Seltenheits-Klassen aus
        if (rarity <= 70) {                               //Wsl 70%
            rarity = 1;
        } else if (rarity <= 90) {                          //Wsl 20% - 1/5
            rarity = 2;
        } else if (rarity > 90) {                          //Wsl 10% - 1/10
            rarity = 3;
        }

        String tDifficulty = "Easy";

        switch (difficulty){
            case "Medium":
                if((int)(Math.random() * 100) >= 33)tDifficulty = "Medium";
                // Zwei Drittel Chance auf Medium, sonst Easy Gegner
                break;
            case "Hard":
                tDifficulty = ((int)(Math.random() * 100) >= 33) ? "Hard" : "Medium";
                //Zwei Drittel Chance auf Hard, sonst Medium Gegner
                break;
            default:
        }
        Log.i("DIFFICULTY: ", "difficulty: " + difficulty + " / tdifficulty: " + tDifficulty);

        HelperCSV helperCSV = new HelperCSV(context);
        List<String[]> list = helperCSV.getDataList("monsterresourcetable");
        Random random = new Random();
        int rand;

        do {
            rand = random.nextInt(list.size());
            name = helperCSV.getString("monsterresourcetable", rand, "Name");
            hp = Integer.parseInt(helperCSV.getString("monsterresourcetable", rand, "HP"));
            dmgMin = Integer.parseInt(helperCSV.getString("monsterresourcetable", rand, "DmgMin"));
            dmgMax = Integer.parseInt(helperCSV.getString("monsterresourcetable", rand, "DmgMax"));
            accuracy = Integer.parseInt(helperCSV.getString("monsterresourcetable", rand, "Accuracy"));
            evasion = Integer.parseInt(helperCSV.getString("monsterresourcetable", rand, "Evasion"));
            critChance = Integer.parseInt(helperCSV.getString("monsterresourcetable", rand, "CritChance"));
            critMultiplier = Double.parseDouble(helperCSV.getString("monsterresourcetable", rand, "CritMultiplier"));
            checkout = helperCSV.getString("monsterresourcetable", rand, "Checkout");
            resistance = Double.parseDouble(helperCSV.getString("monsterresourcetable", rand, "Resistance"));
            armor = Integer.parseInt(helperCSV.getString("monsterresourcetable", rand, "Block"));
            monsterdifficulty = helperCSV.getString("monsterresourcetable", rand, "Difficulty");
            bounty = Integer.parseInt(helperCSV.getString("monsterresourcetable", rand, "Bounty"));

            if(currentBiome.equals(helperCSV.getString("monsterresourcetable", rand, "AllowedBiome1"))){
                run = false;
            }else if (currentBiome.equals(helperCSV.getString("monsterresourcetable", rand, "AllowedBiome2"))){
                run = false;
            }else if (currentBiome.equals(helperCSV.getString("monsterresourcetable", rand, "AllowedBiome3"))){
                run = false;
            }else if (currentBiome.equals("nowhere")){
                run = false;
                Log.e("ERROR@BIOMECHECK","currentBiome == nowhere!");
            }

            Log.i("MONSTERPOOL: ", "New monster created!");
            Log.i("DIFFICULTY: ", "monsterdifficulty: " + monsterdifficulty + " / tdifficulty: " + tDifficulty);
            Log.i("RARITY: ", "rarity: " + rarity + " / monsterrarity: " + Integer.parseInt(helperCSV.getString("monsterresourcetable", rand, "Rarity")));

        }while(rarity != Integer.parseInt(helperCSV.getString("monsterresourcetable", rand, "Rarity")) || run || !monsterdifficulty.equals(tDifficulty));

        //Msg.msgShort(context, heroClass);
        //for(int i = 0; i < list.size(); i++)
        // Msg.msgShort(context, list.get(i)[0] + " " + list.get(i)[1]);
    }


    public int getEvasion(){
        return evasion;
    }

    public int getAccuracy(){return accuracy;}

    public int getCritChance(){
        return critChance;
    }

    public int getHp(){
        return hp;
    }

    public int getDmgMin(){
        return dmgMin;
    }

    public int getDmgMax(){
        return dmgMax;
    }

    public int getArmor(){
        return armor;
    }

    public int getBounty(){
        return bounty;
    }

    public double getResistance(){
        return resistance;
    }

    public double getCritMultiplier(){
        return critMultiplier;
    }

    public String getImgRes(){

        //Besondere Regeln für bestimmte Monster
        switch (name){
            case "":
                break;
            default:
                break;
        }

        String s = name;
        s = s.toLowerCase();
        s = s.replaceAll("ä", "ae");
        s = s.replaceAll("ö", "oe");
        s = s.replaceAll("ü", "ue");
        s = s.replaceAll("-", "_");
        s = s.replaceAll(" ", "_");
        Log.i("MONSTERIMAGE","monster_dummy_" + s);

        return ("monster_dummy_" + s);
    }

    public String getName(){
        return name;
    }

    public String getCheckout(){
        return checkout;
    }

    public String getDifficulty(){
        return monsterdifficulty;
    }
}

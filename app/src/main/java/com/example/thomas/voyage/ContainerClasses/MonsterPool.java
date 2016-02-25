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
    private int image;
    private int block;
    private int evasion;
    private int critChance;
    private int bounty;

    private double resistance;
    private double critMultiplier;
    private double accuracy;

    private String checkout;
    private String name;
    private String monsterdifficulty;

    private boolean run = true;

    public MonsterPool(String currentBiome, String difficulty, Context context ){

        rarity = (int) (Math.random() * 100);           //Wählt eine der Seltenheits-Klassen aus
        if (rarity <= 70) {                               //Wsl 70%
            rarity = 1;
        } else if (rarity <= 80) {                          //Wsl 20% - 1/5
            rarity = 2;
        } else if (rarity >= 90) {                          //Wsl 10% - 1/10
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

        HelperCSV helperCSV = new HelperCSV(context);
        List<String[]> list = helperCSV.getDataList("monsterresourcetable");
        Random random = new Random();
        int rand;

        do {
            rand = random.nextInt(list.size());
            image = rand + 1;
            name = list.get(rand)[2];
            hp = Integer.parseInt(list.get(rand)[4]);
            dmgMin = Integer.parseInt(list.get(rand)[5]);
            dmgMax = Integer.parseInt(list.get(rand)[6]);
            accuracy = Double.parseDouble(list.get(rand)[7]);
            evasion = Integer.parseInt(list.get(rand)[8]);
            critChance = Integer.parseInt(list.get(rand)[9]);
            critMultiplier = Double.parseDouble(list.get(rand)[10]);
            checkout = list.get(rand)[11];
            resistance = Double.parseDouble(list.get(rand)[12]);
            block = Integer.parseInt(list.get(rand)[13]);
            monsterdifficulty = list.get(rand)[17];
            bounty = Integer.parseInt(list.get(rand)[18]);

            if(currentBiome.equals(list.get(rand)[14])){
                run = false;
            }else if (currentBiome.equals(list.get(rand)[15])){
                run = false;
            }else if (currentBiome.equals(list.get(rand)[16])){
                run = false;
            }else if (currentBiome.equals("nowhere")){
                run = false;
                Log.e("ERROR@BIOMECHECK","currentBiome == nowhere!");
            }

        }while(rarity != Integer.parseInt(list.get(rand)[3]) || run || !difficulty.equals(tDifficulty));

        //Msg.msgShort(context, heroClass);
        //for(int i = 0; i < list.size(); i++)
        // Msg.msgShort(context, list.get(i)[0] + " " + list.get(i)[1]);
    }


    public int getEvasion(){
        return evasion;
    }

    public double getAccuracy(){
        return accuracy;
    }

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

    public int getBlock(){
        return block;
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
        return "monster_dummy_" + image;
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

package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class HeroPool {

    private String heroType;
    private String name;
    private String heroClass;
    private String pClass;       //Primärklasse
    private String sClass;       //Sekundärklasse
    private String neededBiome;  //Die Umgebung, die der Held zum spawnen benötigt, überall falls 'null'
    private int rarity;          //Unterteilung der Klassen in Seltenheitsblöcke
    private int imageID;
    private int pHp;
    private int sHp;
    private double pHpWeight = 3;
    private double sHpWeight = 3;
    //HpWeight: Gewichtung des Wertes d. jeweiligen Klasse, vergrößert dessen Bereich im rand.Gen!
    private int pCosts;       //Kosten des Primärklassenbausteins
    private int sCosts;
    private double pCostsWeight = 2;
    private double sCostsWeight = 2;
    private int pEvasion = 95;
    private int sEvasion = 95;
    private double pEvasionWeight = 4;
    private double sEvasionWeight = 4;
    Context context;
    ArrayList<String> biomesRestrictedList = new ArrayList<>();

    public HeroPool(Context con) {
        context = con;
    }

    public String setName() {
        String[] nameArray = {"Gunther", "Gisbert", "Kamel", "Pepe", "Rudy", "Bow", "Joe",
                "Wiesgart", "Knöllchen", "Speck-O", "Toni", "Brieselbert", "Heinmar",
                "Beowulf", "Hartmut von Heinstein", "Konrad Käsebart"};

        //Zufälliger Namens-Generator
        for (boolean run = true; run; ) {
            run = false;

            int count = (int) (Math.random() * 100);
            if (count >= nameArray.length) run = true;
            else name = nameArray[count];

            Log.i("HERONAME", "count : " + count);
        }

        Log.i("HERONAME", "Name des Helden: " + name);
        return name;
    }

    public String setClass(String currentBiome, String type, Context context){

        rarity = (int) (Math.random() * 100);           //Wählt eine der Seltenheits-Klassen aus
        if (rarity <= 60) {                               //Wsl 60%
            rarity = 1;
        } else if (rarity <= 80) {                          //Wsl 20% - 1/5
            rarity = 2;
        } else if (rarity <= 90) {                          //Wsl 10% - 1/10
            rarity = 3;
        } else if (rarity >= 90) {                          //Wsl 10% - 1/10
            rarity = 4;
        }

        HelperCSV helperCSV = new HelperCSV(context);
        List<String[]> list = helperCSV.getDataList("heroresourcetable");
        Random random = new Random();
        int rand;

        do {
            rand = random.nextInt(list.size());
            heroType = list.get(rand)[1];
            Log.i("INFO: ", rand + " / " +  heroType + " / " + rarity + " / " + list.get(rand)[3]);
            imageID = rand + 1;
            if(type.equals("Primary")){
                pClass = list.get(rand)[2];
                pHp = Integer.parseInt(list.get(rand)[4]);
                pCosts = Integer.parseInt(list.get(rand)[5]);
                pEvasion = Integer.parseInt(list.get(rand)[6]);
                pHpWeight = Double.parseDouble(list.get(rand)[7]);
                pCostsWeight = Double.parseDouble(list.get(rand)[8]);
                pEvasionWeight = Double.parseDouble(list.get(rand)[9]);
                heroClass = pClass;
                Log.i("INFO: ", heroClass);
            }else{
                sClass = list.get(rand)[2];
                sHp = Integer.parseInt(list.get(rand)[4]);
                sCosts = Integer.parseInt(list.get(rand)[5]);
                sEvasion = Integer.parseInt(list.get(rand)[6]);
                sHpWeight = Double.parseDouble(list.get(rand)[7]);
                sCostsWeight = Double.parseDouble(list.get(rand)[8]);
                sEvasionWeight = Double.parseDouble(list.get(rand)[9]);
                heroClass = sClass;
                Log.i("INFO: ", heroClass);
            }
        }while(!type.equals(heroType) ||
                rarity != Integer.parseInt(list.get(rand)[3]) ||
                currentBiome.equals(list.get(rand)[10]) ||
                currentBiome.equals(list.get(rand)[11]) ||
                currentBiome.equals(list.get(rand)[12]));

        //Msg.msgShort(context, heroClass);
        //for(int i = 0; i < list.size(); i++)
          // Msg.msgShort(context, list.get(i)[0] + " " + list.get(i)[1]);

        return heroClass;
    }

    public int setRandomVal(int pVal, int sVal, double pValWeight, double sValWeight) {

        Random random = new Random();
        int finalVal;
        int valMax = sVal;
        int valMin = pVal;
        double valMaxWeight = sValWeight;
        double valMinWeight = pValWeight;

        if (pVal > sVal) {
            valMax = pVal;
            valMin = sVal;
            valMaxWeight = pValWeight;
            valMinWeight = sValWeight;
        }

        int valMean = (valMax + valMin) / 2;
        int valMaxArea = (int) ((valMean - valMin) / valMaxWeight);
        int valMinArea = (int) ((valMax - valMean) / valMinWeight);
        int valMaxQuartile = valMean + valMaxArea;
        int valMinQuartile = valMean - valMinArea;
        //Man stelle sich einen Boxplot vor; um den Mittelwert herum wird ein Bereich geschaffen,
        //in dem dann die HP zufällig ermittelt werden

        if(valMaxQuartile == valMinQuartile){
            return valMaxQuartile;
            //Wenn die Werte gleich, returne sie (randNext sonst fehlerhaft)
        }
        finalVal = random.nextInt(valMaxQuartile - valMinQuartile) + valMinQuartile;

        return finalVal;
    }

    public int setHitPoints() {
        return setRandomVal(pHp, sHp, pHpWeight, sHpWeight);
    }

    public int setCosts() {
        return setRandomVal(pCosts, sCosts, pCostsWeight, sCostsWeight);
    }

    public int setEvasion() {
        return setRandomVal(pEvasion, sEvasion, pEvasionWeight, sEvasionWeight);
    }

    public String getImageResource() {
        switch (imageID){
            case 1:
                break;
            default:
                break;
        }
        return ("hero_dummy_" + imageID);
    }
}

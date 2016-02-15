package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class HeroPool {

    private String name;
    private String pClass;       //Primärklasse
    private String sClass;       //Sekundärklasse
    private String neededBiome;  //Die Umgebung, die der Held zum spawnen benötigt, überall falls 'null'
    private int rarity;          //Unterteilung der Klassen in Seltenheitsblöcke
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
        } else if (rarity >= 90) {                          //Wsl 10% - 1/20
            rarity = 4;
        }

        HelperCSV helperCSV = new HelperCSV(context);
        List<String[]> list = helperCSV.getDataList("heroresourcetable");

        Random random = new Random();
        String heroClass = list.get(random.nextInt(list.size() + 1))[2];

       // for(int i = 0; i < list.size(); i++)
         //   Msg.msgShort(context, list.get(i)[0] + " " + list.get(i)[1]);

        return heroClass;
    }

    public String setClassPrimary(String currentBiome) {
        rarity = (int) (Math.random() * 100);           //Wählt eine der Seltenheits-Klassen aus
        if (rarity <= 60) {                               //Wsl 60%
            rarity = 1;
        } else if (rarity <= 80) {                          //Wsl 20% - 1/5
            rarity = 2;
        } else if (rarity <= 90) {                          //Wsl 10% - 1/10
            rarity = 3;
        } else if (rarity >= 90) {                          //Wsl 10% - 1/20
            rarity = 4;
        }
        return setClassP(currentBiome);
    }

    public String setClassP(String currentBiome) {
        switch (rarity) {
            //Je nach Seltenheit wird nun aus einer Primär-Klasse zufällig gezogen
            //Ob-8: Nicht über 100 cases gehen, ohne den random-multiplier zu erhöhen!!
            case 1:
                pCosts = 1000;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            pClass = "Waldläufer";
                            pHp = 300;
                            biomesRestrictedList.add("Dungeon");
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            case 2:
                pCosts = 1500;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            pClass = "Kneipenschläger";
                            pHp = 350;
                            pCosts = 900;
                            pEvasion = 100;
                            pEvasionWeight = 3;
                            neededBiome = null;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            case 3:
                pCosts = 2000;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            pClass = "Monsterjäger";
                            pHp = 380;
                            pHpWeight = 2;
                            neededBiome = null;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            case 4:
                pCosts = 3000;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            pClass = "Sehr Selten";
                            pHp = 400;
                            pCostsWeight = 1;
                            neededBiome = null;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            default:
                break;
        }
        for (int i = 0; i < biomesRestrictedList.size(); i++) {
            if (biomesRestrictedList.get(i).equals(currentBiome)) {
                setClassP(currentBiome);
            }
        }
        return pClass;
    }

    public String setClassSecondary() {
        //Wählt eine der Seltenheits-Klassen aus

        rarity = (int) (Math.random() * 100);
        if (rarity >= 60) {                               //Wsl 60%
            rarity = 1;
        } else if (rarity >= 80) {                          //Wsl 20% - 1/5
            rarity = 2;
        } else if (rarity >= 90) {                          //Wsl 10% - 1/10
            rarity = 3;
        } else if (rarity >= 95) {                          //Wsl 5% - 1/20
            rarity = 4;
        }

        switch (rarity) {
            //Je nach Seltenheit wird nun aus einer Sekundär-Klasse zufällig gezogen

            case 1:
                sCosts = 1000;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            sClass = "Spion";
                            sHp = 200;
                            sHpWeight = 2;
                            sCostsWeight = 3;
                            sEvasion = 90;
                            sEvasionWeight = 2;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            case 2:
                sCosts = 1200;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            sClass = "Schurke";
                            sHp = 300;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            case 3:
                sCosts = 1500;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            sClass = "Glaubenskrieger";
                            sHp = 400;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            case 4:
                sCosts = 2000;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            sClass = "Sehr Seltene Unterklasse";
                            sHp = 400;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            default:
                sClass = "undefiniert";
                break;
        }
        return sClass;
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
        int j = -1;

        for (; !(j >= 0 && j < 8); ) {
            j = (int) (Math.random() * 10);
        }

        return ("hero_dummy_" + j);
    }
}

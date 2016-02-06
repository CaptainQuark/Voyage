package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.util.Log;
import java.util.Random;

import java.util.ArrayList;

public class HeroPool {

    private static String name;
    private static String pClass;       //Primärklasse
    private static String sClass;       //Sekundärklasse
    private static String neededBiome;  //Die Umgebung, die der Held zum spawnen benötigt, überall falls 'null'
    private static int rarity;          //Unterteilung der Klassen in Seltenheitsblöcke
    private static int pHp;
    private static int sHp;
    private static double pHpWeight;
    private static double sHpWeight;
    //HpWeight: Gewichtung des Wertes d. jeweiligen Klasse, vergrößert dessen Bereich im rand.Gen!
    private static int costs;           //Heldenkosten
    private static int costsBase;       //Kosten des Primärklassenbausteins
    private static double costsMultiplier; //Kosten-Multiplikator des Sekundärklassenbausteins
    Context context;
    ArrayList<String> biomes = new ArrayList<>();

    public HeroPool(Context con){
        context = con;
    }

    public static String setName(){
        String[] nameArray = {"Gunther", "Gisbert", "Kamel", "Pepe", "Rudy", "Bow", "Joe",
                "Wiesgart", "Knöllchen", "Speck-O", "Toni", "Brieselbert", "Heinmar",
                "Beowulf","Hartmut von Heinstein", "Konrad Käsebart"};

        //Zufälliger Namens-Generator
        for (boolean run = true; run; ) {
            run = false;

            int count = (int) (Math.random() * 100);
            if( count >= nameArray.length) run = true;
            else name = nameArray[count];

            Log.i("HERONAME", "count : " + count);
        }

        Log.i("HERONAME", "Name des Helden: " + name);
        return name;
    }


    public static String setClassPrimary(String currentBiome) {

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

        switch (rarity) {
            //Je nach Seltenheit wird nun aus einer Primär-Klasse zufällig gezogen
            //Ob-8: Nicht über 100 cases gehen, ohne den random-multiplier zu erhöhen!!
            case 1:
                costsBase = 1000;
                for (boolean run = true; (run && currentBiome.equals("Everywhere"))
                        || (run && neededBiome.equals("Everywhere"))
                        || (run && currentBiome.equals(neededBiome)); ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            pClass = "Waldläufer";
                            pHp = 300;
                            neededBiome = null;
                            break;
                        default:
                            run = true;
                            break;
                    }
                    }
                    break;
            case 2:
                costsBase = 1500;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            pClass = "Kneipenschläger";
                            pHp = 350;
                            neededBiome = null;
                            break;
                        default:
                            run = true;
                            break;
                    }
                    }
                    break;
            case 3:
                costsBase = 2000;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            pClass = "Monsterjäger";
                            pHp = 380;
                            neededBiome = null;
                            break;
                        default:
                            run = true;
                            break;
                    }
                    }
                    break;
            case 4:
                costsBase = 3000;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            pClass = "Sehr Selten";
                            pHp = 400;
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
        return pClass;
    }

    public static String setClassSecondary() {
        //Wählt eine der Seltenheits-Klassen aus

        rarity = (int) (Math.random() * 100);
        if (rarity <= 60) {                               //Wsl 60%
            rarity = 1;
        } else if (rarity <= 80) {                          //Wsl 20% - 1/5
            rarity = 2;
        } else if (rarity <= 90) {                          //Wsl 10% - 1/10
            rarity = 3;
        } else if (rarity <= 95) {                          //Wsl 5% - 1/20
            rarity = 4;
        }

        switch (rarity) {
            //Je nach Seltenheit wird nun aus einer Sekundär-Klasse zufällig gezogen

            case 1:
                costsMultiplier = 1;
                for (boolean run = true; run; ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            sClass = "Spion";
                            sHp = 200;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            case 2:
                costsMultiplier = 1.2;
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
                costsMultiplier = 1.5;
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
                costsMultiplier = 2;
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
                break;
        }
        return sClass;
    }

    public static int setHitPoints() {

        Random random = new Random();
        int hitPoints;
        int hpMax = sHp;
        int hpMin = pHp;
        double hpMaxWeight = sHpWeight;
        double hpMinWeight = pHpWeight;

        if(pHp > sHp){
            hpMax = pHp;
            hpMin = sHp;
            hpMaxWeight = pHpWeight;
            hpMinWeight = sHpWeight;
        }

        int hpMean = (hpMax + hpMin) / 2;
        int hpMaxArea = (int) ((hpMean - hpMin) / hpMaxWeight);
        int hpMinArea = (int) ((hpMax - hpMean) / hpMinWeight);
        int hpMaxQuartile = hpMean + hpMaxArea;
        int hpMinQuartile = hpMean - hpMinArea;
        //Man stelle sich einen Boxplot vor; um den Mittelwert herum wird ein Bereich geschaffen,
        //in dem dann die HP zufällig ermittelt werden

        hitPoints = random.nextInt(hpMaxQuartile - hpMinQuartile) + hpMinQuartile;

        return hitPoints;
    }

    public static int setRandomVal(int pVal, int sVal) {

        Random random = new Random();
        int finalVal;
        int valMax = sVal;
        int valMin = pVal;
        double valMaxWeight = sHpWeight;
        double valMinWeight = pHpWeight;

        if(pHp > sHp){
            valMax = pHp;
            valMin = sHp;
            valMinWeight = pHpWeight;
            valMinWeight = sHpWeight;
        }

        int valMean = (valMax + valMin) / 2;
        int valMaxArea = (int) ((valMean - valMin) / valMinWeight);
        int valMinArea = (int) ((valMax - valMean) / valMinWeight);
        int valMaxQuartile = valMean + valMaxArea;
        int valMinQuartile = valMean - valMinArea;
        //Man stelle sich einen Boxplot vor; um den Mittelwert herum wird ein Bereich geschaffen,
        //in dem dann die HP zufällig ermittelt werden

        finalVal = random.nextInt(valMaxQuartile - valMinQuartile) + valMinQuartile;

        return finalVal;
    }

    public int getCosts() {
        return (int) (costsBase * costsMultiplier);
    }

    public String getImageResource(){
        int j = -1;

        for (; !(j >= 0 && j < 8); ) {
            j = (int) (Math.random() * 10);
        }

        return ("hero_dummy_" + j);
    }
}

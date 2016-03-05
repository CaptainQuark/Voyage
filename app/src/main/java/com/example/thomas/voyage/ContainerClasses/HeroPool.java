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

    public HeroPool(Context con) {
        context = con;
    }

    public String setName() {
        String[] nameArray = {"Gunther", "Gisbert", "Kamel", "Pepe", "Rudy", "Bow", "Joe",
                "Wiesgart", "Knöllchen", "Speck-O", "Toni", "Brieselbert", "Heinmar",
                "Beowulf", "Hartmut von Heinstein", "Konrad Käsebart", "Clayton Wiesel", "Jimmy 'Die Bohne'", "Rob Cross", "Aleksandr Oreshkin"};

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

        rarity = (int) (Math.random() * 1000);           //Wählt eine der Seltenheits-Klassen aus
        if (rarity <= 650) {                               //Wsl 65%
            rarity = 1;
        } else if (rarity <= 850) {                          //Wsl 20% - 1/5
            rarity = 2;
        } else if (rarity <= 950) {                          //Wsl 10% - 1/10
            rarity = 3;
        } else if (rarity >= 950) {                          //Wsl 5% - 1/20
            rarity = 4;
        }

        HelperCSV helperCSV = new HelperCSV(context);
        List<String[]> list = helperCSV.getDataList("heroresourcetable");
        Random random = new Random();
        int rand;

        do {
            rand = random.nextInt(list.size());
            heroType = helperCSV.getString("heroresourcetable", rand, "Type");
            Log.i("INFO: ", rand + " / " +  heroType + " / " + rarity + " / " + list.get(rand)[3]);
            imageID = rand + 1;
            if(type.equals("Primary")){
                pClass = helperCSV.getString("heroresourcetable", rand, "Class");
                pHp = Integer.parseInt(helperCSV.getString("heroresourcetable", rand, "Hp"));
                pCosts = Integer.parseInt(helperCSV.getString("heroresourcetable", rand, "Costs"));
                pEvasion = Integer.parseInt(helperCSV.getString("heroresourcetable", rand, "Evasion"));
                pHpWeight = Double.parseDouble(helperCSV.getString("heroresourcetable", rand, "HpWeight"));
                pCostsWeight = Double.parseDouble(helperCSV.getString("heroresourcetable", rand, "CostsWeight"));
                pEvasionWeight = Double.parseDouble(helperCSV.getString("heroresourcetable", rand, "EvasionWeight"));
                heroClass = pClass;
                Log.i("INFO: ", heroClass);
            }else{
                sClass = helperCSV.getString("heroresourcetable", rand, "Class");
                sHp = Integer.parseInt(helperCSV.getString("heroresourcetable", rand, "Hp"));
                sCosts = Integer.parseInt(helperCSV.getString("heroresourcetable", rand, "Costs"));
                sEvasion = Integer.parseInt(helperCSV.getString("heroresourcetable", rand, "Evasion"));
                sHpWeight = Double.parseDouble(helperCSV.getString("heroresourcetable", rand, "HpWeight"));
                sCostsWeight = Double.parseDouble(helperCSV.getString("heroresourcetable", rand, "CostsWeight"));
                sEvasionWeight = Double.parseDouble(helperCSV.getString("heroresourcetable", rand, "EvasionWeight"));
                heroClass = sClass;
                Log.i("INFO: ", heroClass);
            }
        }while(!type.equals(heroType) ||
                rarity != Integer.parseInt(helperCSV.getString("heroresourcetable", rand, "Rarity")) ||
                currentBiome.equals(helperCSV.getString("heroresourcetable", rand, "RestrictedBiome1")) ||
                currentBiome.equals(helperCSV.getString("heroresourcetable", rand, "RestrictedBiome2")) ||
                currentBiome.equals(helperCSV.getString("heroresourcetable", rand, "RestrictedBiome3")));

        /*

        !!! -> letztes 'currentBiome.equals(list.get(rand)[12]));
            -> Spalte gibt es in CSV-Datei noch nicht, darum jetzt durch 11 ersetzt

         */

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

    public int setBonusNumber(){
        Random r = new Random();
        int returnVal;

        switch(pClass){
            case "Kneipenschläger":
            case "Waldläufer":
                returnVal =  r.nextInt(20) + 1;
                break;
            default:
                returnVal = -1;
        }
        return returnVal;
    }

    public String getImageResource() {

        //Sonderregeln für bestimmte Klassen
        switch (pClass){
            case "Waldläufer":
                break;
            default:
                break;
        }

        String s = pClass;
        s = s.toLowerCase();
        s = s.replaceAll("ä", "ae");
        s = s.replaceAll("ö", "oe");
        s = s.replaceAll("ü", "ue");
        s = s.replaceAll("-", "_");
        s = s.replaceAll(" ", "_");
        Log.i("HEROIMAGE","hero_dummy_" + s);
        return ("hero_dummy_" + s);
    }
}

package com.example.thomas.voyage;

/**
 * Created by Don Maus on 22-Jul-15.
 */
public class HeroPool {

    private static String name;
    private static String pClass;
    private static String sClass;
    private static double rarity;
    private static int hitPoints;
    private static int hpMin;
    private static int hpMax;

    public static String setName(){
        Randomizer randomizer = new Randomizer();

        switch (randomizer.getRandom(1,3)) {
            case 1:  name = "Gunther";
                break;
            case 2:  name = "Gisbert";
                break;
            case 3:  name = "Kamel";
                break;
            default:
                break;
        }
        return name;
    }

    public static String setClassPrimary(){
        Randomizer randomizer = new Randomizer();

        switch (randomizer.getRandom(1,3)) {
            case 1:  pClass = "Waldläufer";
                hpMin=40;
                hpMax=50;
                break;
            case 2:  pClass = "Soldat";
                hpMin=50;
                hpMax=65;
                break;
            case 3:  pClass = "Magus";
                hpMin=40;
                hpMax=45;
                break;
            default:
                break;
        }
        return pClass;
        }

    public static int setHitPoints(){
        Randomizer randomizer = new Randomizer();
        return randomizer.getRandom(hpMin, hpMax);
    }

    public static String setClassSecondary(){
        Randomizer randomizer = new Randomizer();

        switch (randomizer.getRandom(1,3)) {
            case 1:  sClass = "Glaubenskrieger";
                break;
            case 2:  sClass = "Abenteurer";
                break;
            case 3:  sClass = "Schurke";
                break;
            default:
                break;
        }
        return pClass;
    }
}

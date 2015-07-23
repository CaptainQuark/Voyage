package com.example.thomas.voyage;

import android.content.Context;
import android.util.Log;

public class HeroPool {

    private static String name;
    private static String pClass;       //Primärklasse
    private static String sClass;       //Sekundärklasse
    private static String neededBiome;  //Die Umgebung, die der Held zum spawnen benötigt, überall falls 'null'
    private static int rarity;          //Unterteilung der Klassen in Seltenheitsblöcke
    private static int hpMin;           //HP randomness Ober- und Untergrenze, werden durch
    private static int hpMax;           //die Primärklasse vorgegeben


    public static String setName(){                     //Zufälliger Namens-Generator

        for (boolean run = true; run; ) {

            run = false;
            int indicator = (int) (Math.random() * 100);

            switch (indicator) {
                case 1:
                    name = "Gunther";
                    break;
                case 2:
                    name = "Gisbert";
                    break;
                case 3:
                    name = "Kamel";
                    break;
                case 4:
                    name = "Pepe";
                    break;
                case 5:
                    name = "Rudy";
                    break;
                case 6:
                    name = "Bow";
                    break;
                case 7:
                    name = "Joe";
                    break;
                default:
                    run = true;
                    break;
            }
        }

        Log.i("HERONAME", "Name des Helden: " + name);
        return name;
    }


    /*
    public static String setClassPrimary(String currentBiome) {         //Derzeitige Umgebung wird übergeben (sofern auf Pilgerreise, ansonsten null=alles freigeschaltet)
        Randomizer randomizer = new Randomizer();

        rarity = randomizer.getRandom(1, 95);           //Wählt eine der Seltenheits-Klassen aus
        if (rarity <= 60) {                               //Wsl 60%
            rarity = 1;
        } else if (rarity <= 80) {                          //Wsl 20% - 1/5
            rarity = 2;
        } else if (rarity <= 90) {                          //Wsl 10% - 1/10
            rarity = 3;
        } else if (rarity <= 95) {                          //Wsl 5% - 1/20
            rarity = 4;
        }

        while(currentBiome != neededBiome && currentBiome != null && neededBiome==null) {   //Roll wird wiederholt falls falsche Umgebung und Umgebung nicht Standard und nicht keine Bedingungen
            switch (rarity) {                               //Je nach Seltenheit wird nun aus einer Primär-Klasse zufällig gezogen
                case 1:                                     //Wsl 60%
                    switch (randomizer.getRandom(1, 3)) {
                        case 1:
                            pClass = "Waldläufer";
                            hpMin = 40;
                            hpMax = 50;
                            neededBiome = null;
                            break;
                        case 2:
                            pClass = "Soldat";
                            hpMin = 50;
                            hpMax = 65;
                            neededBiome = null;
                            break;
                        case 3:
                            pClass = "Magus";
                            hpMin = 40;
                            hpMax = 45;
                            neededBiome = null;
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:                                     //Wsl 20%
                    switch (randomizer.getRandom(1, 1)) {
                        case 1:
                            pClass = "Ungewöhnlich";
                            hpMin = 40;
                            hpMax = 50;
                            neededBiome = null;
                            break;
                        default:
                            break;
                    }
                    break;
                case 3:                                     //Wsl 10%
                    switch (randomizer.getRandom(1, 1)) {
                        case 1:
                            pClass = "Selten";
                            hpMin = 40;
                            hpMax = 50;
                            neededBiome = null;
                            break;
                        default:
                            break;
                    }
                    break;
                case 4:                                     //Wsl 5%
                    switch (randomizer.getRandom(1, 1)) {
                        case 1:
                            pClass = "Sehr Selten";
                            hpMin = 40;
                            hpMax = 50;
                            neededBiome = null;
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
        return pClass;
    }

    public static int setHitPoints(){                   //Liefert die zufälligen HP, deren Rahmen oben durch die Primär-Klasse gegeben wurde
        Randomizer randomizer = new Randomizer();
        return randomizer.getRandom(hpMin, hpMax);
    }

    public static String setClassSecondary(){
        Randomizer randomizer = new Randomizer();

        rarity = randomizer.getRandom(1, 95);           //Wählt eine der Seltenheits-Klassen aus
        if(rarity <= 60){                               //Wsl 60%
            rarity = 1;
        }
        else if(rarity <= 80){                          //Wsl 20% - 1/5
            rarity = 2;
        }
        else if(rarity <= 90){                          //Wsl 10% - 1/10
            rarity = 3;
        }
        else if(rarity <= 95){                          //Wsl 5% - 1/20
            rarity = 4;
        }

        switch (rarity) {                               //Je nach Seltenheit wird nun aus einer Sekundär-Klasse zufällig gezogen
            case 1:                                     //Wsl 60%
                switch (randomizer.getRandom(1, 3)) {
                    case 1:
                        sClass = "Spion";
                        break;
                    case 2:
                        sClass = "Schurke";
                        break;
                    case 3:
                        sClass = "Glaubenskrieger";
                        break;
                    default:
                        break;
                }
                break;
            case 2:                                     //Wsl 20%
                switch (randomizer.getRandom(1, 1)) {
                    case 1:
                        sClass = "Ungewöhnliche Unterklasse";
                        break;
                    default:
                        break;
                }
                break;
            case 3:                                     //Wsl 10%
                switch (randomizer.getRandom(1, 1)) {
                    case 1:
                        sClass = "Seltene Unterklasse";
                        break;
                    default:
                        break;
                }
                break;
            case 4:                                     //Wsl 5%
                switch (randomizer.getRandom(1, 1)) {
                    case 1:
                        sClass = "Sehr Seltene Unterklasse";
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return sClass;
    }
    */
}

package com.example.thomas.voyage;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HeroPool {

    private static String name;
    private static String pClass;       //Primärklasse
    private static String sClass;       //Sekundärklasse
    private static String neededBiome;  //Die Umgebung, die der Held zum spawnen benötigt, überall falls 'null'
    private static int rarity;          //Unterteilung der Klassen in Seltenheitsblöcke
    private static int hpMin;           //HP randomness Ober- und Untergrenze, werden durch
    private static int hpMax;           //die Primärklasse vorgegeben
    private static int costs;           //Heldenkosten
    private static int costsBase;       //Kosten des Primärklassenbausteins
    private static double costsMultiplier; //Kosten-Multiplikator des Sekundärklassenbausteins
    ArrayList<String> biomes = new ArrayList<String>();

    public static String setName(){                     //Zufälliger Namens-Generator

        for (boolean run = true; run; ) {

            run = false;

            switch ((int) (Math.random() * 100)) {
                //Wert zw. 0 und 100, falls kein case wird der roll wiederholt
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
                for (boolean run = true; (run && currentBiome == "Everywhere")
                        || (run && neededBiome == "Everywhere")
                        || (run && currentBiome == neededBiome); ) {

                    run = false;

                    switch ((int) (Math.random() * 100)) {
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
                            pClass = "Ungewöhnlich";
                            hpMin = 40;
                            hpMax = 50;
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
                            pClass = "Selten";
                            hpMin = 40;
                            hpMax = 50;
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
                            hpMin = 40;
                            hpMax = 50;
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

    public static int setHitPoints() {
        //Liefert die zufälligen HP, deren Rahmen oben durch die Primär-Klasse gegeben wurde
        //Ob-8: Nur wenn hpMin/hpMax zwischen 1 und 100 liegen!!

        int hitPoints;

        do {
            hitPoints = (int) (Math.random() * 100);
        }
        while (hitPoints < hpMin || hitPoints > hpMax);
        return hitPoints;
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
                            break;
                        case 2:
                            sClass = "Schurke";
                            break;
                        case 3:
                            sClass = "Glaubenskrieger";
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
                            sClass = "Ungewöhnliche Unterklasse";
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
                            sClass = "Seltene Unterklasse";
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

    public int getCosts() {
        costs = (int) (costsBase * costsMultiplier);
        return costs;
    }
}
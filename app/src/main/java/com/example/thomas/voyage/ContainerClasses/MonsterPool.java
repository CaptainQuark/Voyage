package com.example.thomas.voyage.ContainerClasses;
import java.util.Random;

public class MonsterPool {

    private int rarity;
    private int hp;
    private int dmgMin;
    private int dmgMax;
    private int image;

    private double accuracy;
    private double evasion;
    private double critChance;

    private String checkout;
    private String name;

    public MonsterPool() {

        rarity = (int) (Math.random() * 100);           //Wählt eine der Seltenheits-Klassen aus
        if (rarity <= 60) {                               //Wsl 60%
            rarity = 1;
        } else if (rarity <= 85) {                          //Wsl 25% - 1/4
            rarity = 2;
        } else if (rarity >= 85) {                          //Wsl 15% - 3/20
            rarity = 3;
        }

        switch (rarity) {
            //Je nach Seltenheit wird nun aus einer Primär-Klasse zufällig gezogen
            //Ob-8: Nicht über 100 cases gehen, ohne den random-multiplier zu erhöhen!!
            case 1:
                for (boolean run = true; run; ) {
                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            name = "Gemeines Monster";
                            checkout = "double";
                            hp = 200;
                            dmgMin = 10;
                            dmgMax = 20;
                            accuracy = 1;
                            evasion = 0.95;
                            critChance = 0;
                            image = 1;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            case 2:
                for (boolean run = true; run; ) {
                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            name = "Uncommon Monster";
                            checkout = "double";
                            hp = 300;
                            dmgMin = 10;
                            dmgMax = 20;
                            accuracy = 1;
                            evasion = 0.99;
                            critChance = 0;
                            image = 2;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            case 3:
                for (boolean run = true; run; ) {
                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            name = "Fufzehn Prozent Monster";
                            checkout = "double";
                            hp = 500;
                            dmgMin = 10;
                            dmgMax = 20;
                            accuracy = 1;
                            evasion = 0.99;
                            critChance = 0;
                            image = 3;
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
    }

    public String getCheckout(){
        return checkout;
    }

    public double getEvasion(){
        return evasion;
    }

    public double getAccuracy(){
        return accuracy;
    }

    public double getCritChance(){
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

    public String getImgRes(){
        return "monster_dummy_" + image;
    }

    public String getName(){
       return name;
    }
}

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
                            name = "Waldläufer";
                            checkout = "master";
                            hp = 50;
                            dmgMin = 10;
                            dmgMax = 20;
                            accuracy = 1;
                            evasion = 0.95;
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
                            name = "Waldläufer";
                            checkout = "master";
                            hp = 50;
                            dmgMin = 10;
                            dmgMax = 20;
                            accuracy = 1;
                            evasion = 0.95;
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
                            name = "Waldläufer";
                            checkout = "master";
                            hp = 50;
                            dmgMin = 10;
                            dmgMax = 20;
                            accuracy = 1;
                            evasion = 0.95;
                            image = 3;
                            break;
                        default:
                            run = true;
                            break;
                    }
                }
                break;
            case 4:
                for (boolean run = true; run; ) {
                    run = false;

                    switch ((int) (Math.random() * 100)) {
                        case 1:
                            name = "Waldläufer";
                            checkout = "master";
                            hp = 50;
                            dmgMin = 10;
                            dmgMax = 20;
                            accuracy = 1;
                            evasion = 0.95;
                            image = 4;
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
        String val = "double";

        return val;
    }

    public int getEvasion(){
        int val = -1;

        return val;
    }

    public int getAccuracy(){
        int val = -1;

        return val;
    }

    public int getCritChance(){
        int val = -1;

        return val;
    }

    public int getHp(){
        int val = 500;

        return val;
    }

    public int getDmgMin(){
        int val = -1;

        return val;
    }

    public int getDmgMax(){
        int val = -1;

        return val;
    }

    public String getImgRes(){
        String val = "monster_dummy_" + image;

        return val;
    }

    public String getName(){
        Random rand = new Random();
        String[] names = {"Magmamemnnon", "Knöllchen", "Steinchen"};
        int size = names.length, index = 0;

        for(boolean check = true; check; ){

            // erzeuge Zufallswert von 0 - 2
            // wenn zwischen 5 - 10, dann: int randomValue = ran.nextInt(6) + 5;
            //  -> Randomwert zw. 0 und 5 wird mit 5 addiert
            index = rand.nextInt(size);
            check = false;
        }

        return names[index];
    }
}

package com.example.thomas.voyage;

/**
 * Created by Don Maus on 22-Jul-15.
 */
public class HeroPool {

    private static String name;
    private static String pClass;
    private static String sClass;
    private static int rarity;
    private static int hitPoints;
    private static int hpMin;
    private static int hpMax;


    public static String setName(){                     //Zufälliger Namens-Generator
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

    public static String setClassPrimary() {
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

        switch (rarity) {                               //Je nach Seltenheit wird nun aus einer Primär-Klasse zufällig gezogen
            case 1:                                     //Wsl 60%
                switch (randomizer.getRandom(1, 3)) {
                case 1:
                    pClass = "Waldläufer";
                    hpMin = 40;
                    hpMax = 50;
                    break;
                case 2:
                    pClass = "Soldat";
                    hpMin = 50;
                    hpMax = 65;
                    break;
                case 3:
                    pClass = "Magus";
                    hpMin = 40;
                    hpMax = 45;
                    break;
                default:
                    break;
                }
                break;
            case 2:                                     //Wsl 20%
                switch (randomizer.getRandom(1, 3)) {
                    case 1:
                        pClass = "Ungewöhnlich";
                        hpMin = 40;
                        hpMax = 50;
                        break;
                    default:
                        break;
                }
                break;
            case 3:                                     //Wsl 10%
                switch (randomizer.getRandom(1, 3)) {
                    case 1:
                        pClass = "Selten";
                        hpMin = 40;
                        hpMax = 50;
                        break;
                    default:
                        break;
                }
                break;
            case 4:                                     //Wsl 5%
                switch (randomizer.getRandom(1, 3)) {
                    case 1:
                        pClass = "Sehr Selten";
                        hpMin = 40;
                        hpMax = 50;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
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
                        pClass = "Spion";
                        break;
                    case 2:
                        pClass = "Schurke";
                        break;
                    case 3:
                        pClass = "Glaubenskrieger";
                        break;
                    default:
                        break;
                }
                break;
            case 2:                                     //Wsl 20%
                switch (randomizer.getRandom(1, 3)) {
                    case 1:
                        pClass = "Ungewöhnliche Unterklasse";
                        break;
                    default:
                        break;
                }
                break;
            case 3:                                     //Wsl 10%
                switch (randomizer.getRandom(1, 3)) {
                    case 1:
                        pClass = "Seltene Unterklasse";
                        break;
                    default:
                        break;
                }
                break;
            case 4:                                     //Wsl 5%
                switch (randomizer.getRandom(1, 3)) {
                    case 1:
                        pClass = "Sehr Seltene Unterklasse";
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
}

package com.example.thomas.voyage;

/**
 * Created by Don Maus on 22-Jul-15.
 */
public class Hero {

    private static String heroName;
    private static int hitPoints;
    private static String classPrimary;
    private static String classSecondary;

    public Hero(){                          //Konstruktor, Initialize seperat für spätere Zwecke
        Initialize(null);
    }

    public static void Initialize(String biome){        //Platzhalter: in Klammer, welches Biome (in Blöcken)

        HeroPool heropool = new HeroPool();
        Randomizer randomizer = new Randomizer();

        heroName = heropool.setName();
        classPrimary=heropool.setClassPrimary(biome);
        hitPoints = heropool.setHitPoints();
        classSecondary=heropool.setClassSecondary();

    }

}

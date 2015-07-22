package com.example.thomas.voyage;

/**
 * Created by Don Maus on 22-Jul-15.
 */
public class Hero {

    private static String heroName;
    private static int hitPoints;
    private static String classPrimary;
    private static String classSecondary;

    public Hero(){
        Initialize();
    }

    public static void Initialize(){        //Platzhalter: in Klammer, welches Biome (in Blöcken)

        HeroPool heropool = new HeroPool();
        Randomizer randomizer = new Randomizer();


       // hitPoints = randomizer.getRandom(30, 50);
        heroName = heropool.setName();
        classPrimary=heropool.setClassPrimary();
        hitPoints = heropool.setHitPoints();

    }

}

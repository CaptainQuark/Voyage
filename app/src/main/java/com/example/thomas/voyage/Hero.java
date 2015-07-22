package com.example.thomas.voyage;

/**
 * Created by Don Maus on 22-Jul-15.
 */
public class Hero {

    String Name;
    int hitPoints;
    String classPrimary;
    String classSecondary;

    public static void Initialize(){        //Platzhalter: in Klammer, welches Biome (in Blöcken)

        Hero hero = new Hero();
        HeroPool heropool = new HeroPool();
        Randomizer randomizer = new Randomizer();


        hero.hitPoints = randomizer.getRandom(30, 50);
    }

}
